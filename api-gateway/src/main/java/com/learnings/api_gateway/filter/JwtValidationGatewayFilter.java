package com.learnings.api_gateway.filter;

import com.learnings.api_gateway.security.JwtIntrospectionClient;
import com.learnings.api_gateway.security.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtValidationGatewayFilter implements GlobalFilter, Ordered {

    private final JwtTokenValidator jwtTokenValidator;
    private final JwtIntrospectionClient introspectionClient;
    @Value("${auth.introspection.enabled:false}")
    private boolean introspectionEnabled;
    @Value("${auth.introspection.url}")
    private String introspectionUrl;

    private static final List<String> WHITELIST = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/actuator"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Executing JwtValidationGatewayFilter filter");
        String path = exchange.getRequest().getURI().getPath();

        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);
        if (authHeaders.isEmpty()) {
            return unauthorized(exchange, "missing_authorization_header");
        }
        String authHeader = authHeaders.get(0);
        if (!authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "invalid_authorization_header");
        }
        String token = authHeader.substring(7);

        Jwt jwt;
        try {
            jwt = jwtTokenValidator.validateToken(token);
        } catch (Exception e) {
            log.warn("JWT local validation failed: {}", e.getMessage());
            return unauthorized(exchange, "invalid_or_expired_token");
        }

        Long userId = jwtTokenValidator.getSubjectAsLong(jwt);
        String username = jwtTokenValidator.getUsername(jwt);
        List<String> roles = jwtTokenValidator.getRoles(jwt);
        String jti = Optional.ofNullable(jwt.getId()).orElse("");

        if (!introspectionEnabled) {
            return forwardWithHeaders(exchange, chain, userId, username, roles, jti, jwt.getClaims());
        }

        // call auth-service introspect
        return introspectionClient.introspect(introspectionUrl, authHeader)
                .flatMap(map -> {
                    Object activeObj = map.get("active");
                    boolean active = Boolean.TRUE.equals(activeObj);
                    if (!active) {
                        log.warn("Introspection: token inactive");
                        return unauthorized(exchange, "token_inactive");
                    }
                    // forward using claims from local decode (or use map.get("claims"))
                    Map<String,Object> claims = (Map<String,Object>) map.get("claims");
                    return forwardWithHeaders(exchange, chain, userId, username, roles, jti, claims);
                })
                .onErrorResume(ex -> {
                    log.error("Introspection call failed: {}", ex.getMessage());
                    return unauthorized(exchange, "introspection_error");
                });
    }

    private Mono<Void> forwardWithHeaders(ServerWebExchange exchange, GatewayFilterChain chain,
                                          Long userId, String username, List<String> roles, String jti, Map<String,Object> claims) {

        List<SimpleGrantedAuthority> authorities = roles == null ? List.of() :
                roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toList());

        Authentication auth = new UsernamePasswordAuthenticationToken(String.valueOf(userId), null, authorities);
        var builder = exchange.getRequest().mutate();
        if (userId != null) builder.header("X-User-Id", String.valueOf(userId));
        if  (username != null) builder.header("X-User-Username", username);
        if (roles != null && !roles.isEmpty()) builder.header("X-User-Roles", String.join(",", roles));
        if (jti != null) builder.header("X-JTI", jti);
        builder.header("X-Auth-Claims", serializeClaims(claims));

        var mutatedExchange = exchange.mutate().request(builder.build()).build();
        return chain.filter(mutatedExchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
    }

    private String serializeClaims(Map<String, Object> claims) {
        if (claims == null || claims.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        claims.forEach((k, v) -> {
            sb.append("\"").append(k).append("\":\"").append(v).append("\",");
        });
        if (sb.charAt(sb.length()-1) == ',') sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        return sb.toString();
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String reason) {
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = ("{\"error\":\"" + reason + "\"}").getBytes();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    private boolean isWhitelisted(String path) {
        if (!StringUtils.hasText(path)) return false;
        for (String p : WHITELIST) {
            if (path.startsWith(p)) return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}


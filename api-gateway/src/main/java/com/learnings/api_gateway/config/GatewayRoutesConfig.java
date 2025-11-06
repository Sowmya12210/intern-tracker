package com.learnings.api_gateway.config;

import com.learnings.api_gateway.enums.RoleName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import java.util.Arrays;

@Configuration
@Slf4j
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-route", r -> r
                        .path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE")
                )
                .route("intern-route", r -> r
                        .path("/api/interns/**")
                        .filters(f -> f
                                .filter((exchange, chain) -> {
                                    boolean hasValidRole = doRoleCheck(exchange);
                                    if(!hasValidRole){
                                        log.error("User is not having valid role to perform the operation");
                                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                        return exchange.getResponse().setComplete();
                                    }
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("lb://INTERN-SERVICE")
                )
                .build();
    }

    private boolean doRoleCheck(ServerWebExchange exchange) {
        String rolesHeader =  exchange.getRequest().getHeaders().getFirst("X-User-Roles");
        if(rolesHeader==null || rolesHeader.isEmpty()) return true;
        String path =  exchange.getRequest().getURI().getPath();
        HttpMethod httpMethod = exchange.getRequest().getMethod();
        if (path.startsWith("/api/interns/me")) {
            return hasRole(rolesHeader, RoleName.MENTOR);
        }
        if (path.startsWith("/api/interns/feedback") && httpMethod.equals(HttpMethod.POST)) {
            return hasRole(rolesHeader, RoleName.MENTOR);
        }
        if(path.startsWith("/api/interns") && httpMethod.equals(HttpMethod.POST)) {
            return hasRole(rolesHeader, RoleName.ADMIN);
        }
        if(path.contains("/assign-mentor") && httpMethod.equals(HttpMethod.PATCH)) {
            return hasRole(rolesHeader, RoleName.ADMIN);
        }
        return true;
    }

    private boolean hasRole(String rolesHeader, RoleName requiredRole) {
        if (rolesHeader == null) return false;
        return Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .anyMatch(r -> r.equalsIgnoreCase(requiredRole.name()) || r.equalsIgnoreCase("ROLE_" + requiredRole.name()));
    }
}


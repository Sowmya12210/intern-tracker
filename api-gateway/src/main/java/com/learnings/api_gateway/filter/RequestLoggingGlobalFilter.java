package com.learnings.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RequestLoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String correlation = request.getHeaders().getFirst("X-Correlation-Id");
        if (correlation == null || correlation.isBlank()) {
            correlation = UUID.randomUUID().toString();
            exchange.getRequest().mutate().header("X-Correlation-Id", correlation);
        }

        String headersSummary = request.getHeaders().entrySet().stream()
                .filter(e -> !e.getKey().equalsIgnoreCase("authorization")) // hide Authorization value in logs
                .map(e -> e.getKey() + "=" + e.getValue().stream().collect(Collectors.joining(",")))
                .collect(Collectors.joining("; "));

        log.info("[{}] Incoming request: method={}, path={}, remote={}, headers=[{}]",
                correlation,
                request.getMethod(),
                request.getURI().getPath(),
                request.getRemoteAddress(),
                headersSummary);

        String finalCorrelation = correlation;
        return chain.filter(exchange)
                .doOnSuccess(unused -> log.info("[{}] Completed request: path={}", finalCorrelation, request.getURI().getPath()));
    }

    @Override
    public int getOrder() {
        return -1; // run early
    }
}


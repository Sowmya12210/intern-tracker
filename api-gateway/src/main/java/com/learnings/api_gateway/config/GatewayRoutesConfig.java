package com.learnings.api_gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                        .uri("lb://INTERN-SERVICE")
                )
                .build();
    }
}


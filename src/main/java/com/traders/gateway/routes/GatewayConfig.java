package com.traders.gateway.routes;

import com.traders.gateway.security.jwt.JWTRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    private final JWTRelayGatewayFilterFactory jwtRelayGatewayFilterFactory;

    public GatewayConfig(JWTRelayGatewayFilterFactory jwtRelayGatewayFilterFactory) {
        this.jwtRelayGatewayFilterFactory = jwtRelayGatewayFilterFactory;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("authService", r -> r
                .path("/api/auth/login")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/auth/login", "/api/authenticate")
                )
                .uri("lb://authService")
            )

            .route("authService", r -> r
                .path("/api/auth/greet")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/auth/greet", "/api/greet")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://authService")
            )
            .build();
    }
}

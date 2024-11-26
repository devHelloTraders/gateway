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
////////////////////////////////////////////
            .route("portfolioservice", r -> r
                .path("/api/portfolio/get")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/portfolio/get", "/api/portfolio")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
///////////////////////////////////////////
            .route("portfolioservice", r -> r
                .path("/api/portfolio/transactions")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/portfolio/transactions", "/api/transactions")


                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
            .path("/api/portfolio/admin/transactions")
            .and()
            .method("GET")
            .filters(f -> f
                .rewritePath("/api/portfolio/admin/transactions", "/api/transactionsForUser")
                .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
            )
            .uri("lb://portfolioservice")
        )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/transactions/add")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/portfolio/transactions/add", "/api/transactions/add")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
//////////////////////////////////////////////////

            .route("portfolioservice", r -> r
                .path("/api/portfolio/watchlist")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/portfolio/watchlist", "/api/watchlist")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/admin/watchlist")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/portfolio/admin/watchlist", "/api/watchlistForUser")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/watchlist/remove")
                .and()
                .method("DELETE")
                .filters(f -> f
                    .rewritePath("/api/portfolio/watchlist/remove", "/api/watchlist/remove")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/watchlist/add")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/portfolio/watchlist/add", "/api/watchlist/add")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/watchlist/update")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/portfolio/watchlist/update", "/api/watchlist/update")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .build();
    }
}

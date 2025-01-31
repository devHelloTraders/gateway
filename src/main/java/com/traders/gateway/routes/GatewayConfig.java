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

            .route("authService", r -> r
                .path("/api/auth/change-password")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/auth/change-password", "/api/account/change-password")
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

            .route("portfolioservice", r -> r
                .path("/api/portfolio/gethistory")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/portfolio/gethistory", "/api/portfolio/history")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/admin/portfolio")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/admin/portfolio", "/api/admin/portfolioForUser")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/add-deposit-request")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/portfolio/add-deposit-request", "/api/wallet/add-deposit-request")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/add-withdraw-request")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/portfolio/add-withdraw-request", "/api/wallet/add-withdraw-request")
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
                .rewritePath("/api/portfolio/admin/transactions", "/api/admin/transactionsForUser")
                .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
            )
            .uri("lb://portfolioservice")
        )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/transactions/addTxn")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/portfolio/transactions/addTxn", "/api/transactions/add")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )


            .route("portfolioservice", r -> r
                .path("/api/portfolio/transactions/update")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/portfolio/transactions/update", "/api/transactions/update")
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
                    .rewritePath("/api/portfolio/admin/watchlist", "/api/admin/watchlistForUser")
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
            .route("portfolioservice", r -> r
                .path("/api/portfolio/wallet/deposit-requests")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/portfolio/wallet/deposit-requests", "/api/wallet/deposit-requests")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/wallet/withdraw-requests")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/portfolio/wallet/withdraw-requests", "/api/wallet/withdraw-requests")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
            .route("portfolioservice", r -> r
                .path("/api/portfolio/exposure")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/portfolio/exposure", "/api/portfolio/exposure")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://portfolioservice")
            )
    //////////////////////////////////////////////////////////////////////////////////
            .route("exchangeService", r -> r
                .path("/api/exchange/renew")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/exchange/renew", "/api/admin/renewsession")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/exchange/start")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/exchange/start", "/api/admin/startwebsocket")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/exchange/restart")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/exchange/restart", "/api/admin/renewWebSocket")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/exchange/stop")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/exchange/stop", "/api/admin/stopwebsocket")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/exchange/getStocks")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/exchange/getStocks", "/api/stocks/getAll")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/exchange/search")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/exchange/search", "/api/stocks/search")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/exchange/machine/subscribe")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/exchange/machine/subscribe", "/api/machine/subscribe")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/exchange/machine/map")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/exchange/machine/map", "/api/machine/map")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/exchange/machine/quotes")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/exchange/machine/quotes", "/api/machine/quotes")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            //////////////////////////////////////////////////////////////

            .route("exchangeService", r -> r
                .path("/ws/market")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/ws/market", "/ws/update")
                  //  .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/app/subscribe")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/app/subscribe", "/app/subscribe")
                 //   .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )

            .route("exchangeService", r -> r
                .path("/api/portfolio/transactions/add")
                .and()
                .method("POST")
                .filters(f -> f
                    .rewritePath("/api/portfolio/transactions/add", "/api/exchange/trade/add")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://exchangeService")
            )


            ////////////////////////////////////////////////
            .route("admin-service", r -> r
                .path("/api/admin/register")
                .and()
                .method("POST")
                .filters(f -> f
                        .rewritePath("/api/admin/register", "/api/admin/user/register")
                        .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/activate-user")
                .and()
                .method("PUT")
                .filters(f -> f
                    .rewritePath("/api/admin/activate-user", "/api/admin/user/activate")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/deactivate-user")
                .and()
                .method("PUT")
                .filters(f -> f
                    .rewritePath("/api/admin/deactivate-user", "/api/admin/user/deactivate")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/default-conf/**")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/admin/default-conf/(?<authority>.*)", "/api/admin/config/default/${authority}")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/conf/**")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/admin/conf/(?<userId>.*)", "/api/admin/config/get/${userId}")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/deposit-request")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/admin/deposit-request", "/api/admin/deposit-requests/")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/deposit-request/approve")
                .and()
                .method("PUT")
                .filters(f -> f
                    .rewritePath("/api/admin/deposit-request/approve", "/api/admin/deposit-requests/approve")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/deposit-request/reject")
                .and()
                .method("PUT")
                .filters(f -> f
                    .rewritePath("/api/admin/deposit-request/reject", "/api/admin/deposit-requests/reject")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/withdraw-requests")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/admin/withdraw-requests", "/api/admin/withdraw-requests/")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/withdraw-request/approve")
                .and()
                .method("PUT")
                .filters(f -> f
                    .rewritePath("/api/admin/withdraw-request/approve", "/api/admin/withdraw-requests/approve")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/withdraw-request/reject")
                .and()
                .method("PUT")
                .filters(f -> f
                    .rewritePath("/api/admin/withdraw-request/reject", "/api/admin/withdraw-requests/reject")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/trading-clients")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/admin/trading-clients", "/api/admin/user/trading-clients")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/brokers")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/admin/brokers", "/api/admin/user/brokers")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/admin/trading-clients/**")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/admin/trading-clients/(?<userId>.*)", "/api/admin/user/trading-clients/${userId}")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .route("admin-service", r -> r
                .path("/api/auth/trading-client/conf")
                .and()
                .method("GET")
                .filters(f -> f
                    .rewritePath("/api/auth/trading-client/conf", "/api/conf/getconf")
                    .filter(jwtRelayGatewayFilterFactory.apply(new Object()))
                )
                .uri("lb://admin-service")
            )
            .build();
    }
}

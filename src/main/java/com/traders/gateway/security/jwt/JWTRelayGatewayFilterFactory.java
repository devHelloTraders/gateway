package com.traders.gateway.security.jwt;

import com.traders.gateway.config.SecurityJwtConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.PROXY_AUTHORIZATION;

@Component
public class JWTRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static final String BEARER = "Bearer ";

    private final SecurityJwtConfiguration jwtConfiguration;
    public JWTRelayGatewayFilterFactory( SecurityJwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String bearerToken = exchange.getRequest().getHeaders().getFirst(PROXY_AUTHORIZATION);
            System.out.println("Authorization header: " + bearerToken);
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = this.extractToken(bearerToken);
                return jwtConfiguration.getReactiveJwtDecoderInstance().decode(token)
                    .thenReturn(withBearerAuth(exchange, token))
                    .flatMap(chain::filter);
            }

            return chain.filter(exchange); // Proceed without the token if it's not present
        };
    }

    private String extractToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.length() > 7 && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("Invalid token in Authorization header");
    }

    private ServerWebExchange withBearerAuth(ServerWebExchange exchange, String authorizeToken) {
        return exchange.mutate().request(r -> r.headers(headers -> headers.setBearerAuth(authorizeToken))).build();
    }
}

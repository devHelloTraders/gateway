package com.traders.gateway.security.jwt;

import com.traders.gateway.config.RedisService;
import com.traders.gateway.config.SecurityJwtConfiguration;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JWTRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static final String BEARER = "Bearer ";
    private final RedisService redisService;
    private final SecurityJwtConfiguration jwtConfiguration;
    public JWTRelayGatewayFilterFactory(RedisService redisService, SecurityJwtConfiguration jwtConfiguration) {
        this.redisService = redisService;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = this.extractToken(bearerToken);
                return jwtConfiguration.getReactiveJwtDecoderInstance().decode(token)
                    .flatMap(jwt -> chain.filter(withBearerAuth(exchange, token, jwt.getClaimAsString("userId"),jwt.getClaim("creationTimeStamp"))));
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

    private ServerWebExchange withBearerAuth(ServerWebExchange exchange, String authorizeToken, String userId, Object creationStamp) {
        Optional<Long> tokenDetails = Optional.ofNullable(redisService.getCustomCacheValue("tokenManager", userId));

        if (creationStamp instanceof Long creationTime && tokenDetails.orElse(Long.MIN_VALUE) <= creationTime) {
            redisService.saveToCustomCacheWithTTL("ActiveUsersCache", userId, userId, 3, TimeUnit.MINUTES);

            return exchange.mutate().request(r -> r.headers(headers -> {
                headers.setBearerAuth(authorizeToken);
                headers.set("X-User-Id", userId);
            })).build();
        }

        return exchange.mutate().request(r -> r.headers(headers -> {
            headers.setBearerAuth("");
            headers.remove("X-User-Id");
        })).build();
    }
}

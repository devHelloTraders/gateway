package com.traders.gateway.config;

import com.traders.common.security.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class SecurityJwtConfiguration extends com.traders.common.config.SecurityJwtConfiguration {
    public ReactiveJwtDecoder getReactiveJwtDecoderInstance() {
        return NimbusReactiveJwtDecoder.withSecretKey(this.getSecretKey()).macAlgorithm(SecurityUtils.JWT_ALGORITHM).build();
    }
}

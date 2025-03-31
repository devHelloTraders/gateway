package com.traders.gateway.config;

import com.traders.common.constants.AuthoritiesConstants;
import com.traders.gateway.properties.ConfigProperties;
import com.traders.gateway.web.filter.SpaWebFilter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private final ConfigProperties configProperties;
    private final SecurityJwtConfiguration jwtConfiguration;

    public SecurityConfiguration(ConfigProperties configProperties, SecurityJwtConfiguration jwtConfiguration) {
        this.configProperties = configProperties;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(SecurityProperties properties) {
        SecurityProperties.User user = properties.getUser();
        UserDetails userDetails = User.withUsername(user.getName())
            .password("{noop}" + user.getPassword())
            .roles(StringUtils.toStringArray(user.getRoles()))
            .build();
        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .securityMatcher(
                new NegatedServerWebExchangeMatcher(
                    new OrServerWebExchangeMatcher(pathMatchers("/app/**", "/i18n/**", "/content/**", "/swagger-ui/**"))
                )
            )
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .addFilterAfter(new SpaWebFilter(), SecurityWebFiltersOrder.HTTPS_REDIRECT)
            .headers(headers ->
                headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(configProperties.getSecurity().getContentSecurityPolicy()))
                    .frameOptions(frameOptions -> frameOptions.mode(Mode.DENY))
                    .referrerPolicy(referrer ->
                        referrer.policy(ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                    )
                    .permissionsPolicy(permissions ->
                        permissions.policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
                        )
                    )
            )
            .authorizeExchange(authz ->
                // prettier-ignore
                authz
                    .pathMatchers("/").permitAll()
                    .pathMatchers("/*.*").permitAll()
                    .pathMatchers("/ws/**").permitAll()
                    .pathMatchers("/open/**").permitAll()
                    .pathMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .pathMatchers("/api/exchange/machine/**").hasAuthority(AuthoritiesConstants.MACHINE)
                    .pathMatchers("/api/portfolio/machine/**").hasAuthority(AuthoritiesConstants.MACHINE)
                    .pathMatchers("/api/auth/**").permitAll()
                    .pathMatchers("/api/auth/secure/**").authenticated()
                    .pathMatchers("/api/portfolio/**").authenticated()
                    .pathMatchers("/api/exchange/**").authenticated()
                    .pathMatchers("/api/client/**").hasAuthority(AuthoritiesConstants.USER)
                    // microfrontend resources are loaded by webpack without authentication, they need to be public
                    .pathMatchers("/services/*/*.js").permitAll()
                    .pathMatchers("/services/*/*.txt").permitAll()
                    .pathMatchers("/services/*/*.json").permitAll()
                    .pathMatchers("/services/*/*.js.map").permitAll()
                    .pathMatchers("/services/*/management/health/readiness").permitAll()
                    .pathMatchers("/services/*/v3/api-docs").hasAuthority(AuthoritiesConstants.ADMIN)
                    .pathMatchers("/services/**").authenticated()
                    .pathMatchers("/v3/api-docs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .pathMatchers("/management/health").permitAll()
                    .pathMatchers("/management/gateway/**").permitAll()
                    .pathMatchers("/management/health/**").permitAll()
                    .pathMatchers("/management/info").permitAll()
                    .pathMatchers("/management/prometheus").permitAll()
                    .pathMatchers("/api/login").permitAll()
                    .pathMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            )
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtDecoder(jwtConfiguration.getReactiveJwtDecoderInstance())) // Use the custom JwtDecoder
            );
        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}

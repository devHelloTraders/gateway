package com.traders.gateway.config;

import com.traders.common.properties.ConfigProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CommonBeanService {
    private final RedissonConfig redissonConfig;

    public CommonBeanService(RedissonConfig redissonConfig) {
        this.redissonConfig = redissonConfig;
    }

    @Bean
    public ConfigProperties getConfigProperties(){
        return new ConfigProperties();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedissonClient getRedissonCLient(ConfigProperties configProperties){
        return Redisson.create(redissonConfig.getRedisConfig(configProperties));
    }
}

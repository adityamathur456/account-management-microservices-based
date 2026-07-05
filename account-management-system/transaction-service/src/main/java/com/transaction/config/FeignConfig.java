package com.transaction.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor authRequestInterceptor() {
        return (RequestTemplate template) -> {
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() instanceof Jwt jwt) {
                String tokenValue = jwt.getTokenValue();
                template.header("Authorization", "Bearer %s".formatted(tokenValue));
            }
        };
    }
}

package com.beginner.rest.webservices.restfulwebservices.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

// This class is created in order to override Spring security's filter chain
@Configuration
public class SpringSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. all requests are authenticated
        // 3. disable CSRF to enable POST/PUT
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated()).csrf(AbstractHttpConfigurer::disable);

        // 2. show popup if not auth
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

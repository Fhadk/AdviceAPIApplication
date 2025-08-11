package com.descenedigital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF completely
            .csrf(AbstractHttpConfigurer::disable)
            
            // Disable form login completely
            .formLogin(AbstractHttpConfigurer::disable)
            
            // Disable logout
            .logout(AbstractHttpConfigurer::disable)
            
            // Disable HTTP Basic authentication temporarily for testing
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // Configure authorization rules - ALLOW ALL for now
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
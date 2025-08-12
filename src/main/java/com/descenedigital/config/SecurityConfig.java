package com.descenedigital.config;

import com.descenedigital.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


    @Configuration
    @EnableMethodSecurity(prePostEnabled = true)
    public  class SecurityConfig {
      private final JwtAuthFilter jwt;
      public SecurityConfig(JwtAuthFilter jwt){ this.jwt=jwt; }

        @Bean
        SecurityFilterChain security(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
            return http
                    .csrf(cs -> cs.ignoringRequestMatchers("/h2-console/**","/v3/api-docs/**","/swagger-ui/**"))
                    .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(a -> a
                            .requestMatchers("/api/auth/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui/index.html","/h2/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .headers(h -> h.frameOptions(f -> f.sameOrigin())) // H2 console
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
      @Bean
      PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
      @Bean
      AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception { return c.getAuthenticationManager(); }
    }

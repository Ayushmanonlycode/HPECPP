package com.showdown.springboot.cartservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Cart Service.
 *
 * <p>Since NGINX is the gateway and simply forwards the Authorization header,
 * this service validates the JWT itself using the Keycloak public keys.
 * All cart operations require a valid Bearer token.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Actuator health/metrics are accessible without a token
                        .requestMatchers("/actuator/**").permitAll()
                        // Allow anonymous cart access for guest sessions
                        .requestMatchers("/api/cart/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})  // issuer-uri set in application.properties
                )
                .build();
    }
}

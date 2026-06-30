package com.showdown.springboot.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * Security configuration for the API Gateway.
 *
 * <p>The gateway is the single trust boundary. It validates the JWT on every
 * inbound request and propagates the token to downstream services via the
 * Authorization header. Downstream services do NOT re-validate tokens.
 *
 * <p>Public routes (registration, login, actuator) are open. All other routes
 * require a valid Bearer JWT issued by the configured Keycloak realm.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // Disable session — gateway is stateless
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                // CSRF not needed for a stateless REST API gateway
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // User registration and login are public
                        .pathMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        // Catalog browsing is public (read-only)
                        .pathMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/items/**").permitAll()
                        // Actuator health and metrics are internal — permit all from within Docker network
                        .pathMatchers("/actuator/**").permitAll()
                        // Everything else requires a valid JWT
                        .anyExchange().authenticated()
                )
                // Configure JWT resource server — validates against Keycloak JWKS endpoint
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})  // issuer-uri configured in application.yml
                )
                .build();
    }
}

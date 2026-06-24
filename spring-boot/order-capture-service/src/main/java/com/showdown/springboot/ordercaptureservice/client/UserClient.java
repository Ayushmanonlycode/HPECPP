package com.showdown.springboot.ordercaptureservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);
    
    private final WebClient webClient;

    public UserClient(WebClient.Builder webClientBuilder,
                      @Value("${services.user.url}") String userServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(userServiceUrl)
                .build();
    }

    public boolean checkUserExists(String userId) {
        log.debug("Checking if user exists: {}", userId);
        try {
            webClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound ex) {
            log.warn("User {} not found", userId);
            return false;
        } catch (WebClientResponseException ex) {
            log.error("Failed to check user {}: {} {}", userId, ex.getStatusCode(), ex.getMessage());
            // Fail closed: reject order if we can't verify
            return false;
        } catch (Exception ex) {
            log.error("Unexpected error checking user {}: {}", userId, ex.getMessage());
            return false;
        }
    }
}

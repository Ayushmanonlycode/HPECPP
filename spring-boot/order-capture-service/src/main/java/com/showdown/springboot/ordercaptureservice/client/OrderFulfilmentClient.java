package com.showdown.springboot.ordercaptureservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class OrderFulfilmentClient {

    private static final Logger log = LoggerFactory.getLogger(OrderFulfilmentClient.class);
    private static final String CB_NAME = "fulfilment-service";

    private final WebClient webClient;

    public OrderFulfilmentClient(WebClient.Builder webClientBuilder,
                                 @Value("${services.fulfilment.url:http://sb-order-fulfilment-service:8085}") String fulfilmentUrl) {
        this.webClient = webClientBuilder.baseUrl(fulfilmentUrl).build();
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "createFulfilmentFallback")
    @Retry(name = CB_NAME)
    public boolean createFulfilment(String orderId) {
        try {
            Map<?, ?> response = webClient.post()
                    .uri("/api/fulfilments")
                    .bodyValue(Map.of("orderId", orderId))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null;
        } catch (Exception e) {
            log.error("Error creating fulfilment for order ID: {}", orderId, e);
            return false;
        }
    }

    @SuppressWarnings("unused")
    public boolean createFulfilmentFallback(String orderId, Throwable t) {
        log.error("Fulfilment service unavailable for order ID {} — circuit open. Reason: {}", orderId, t.getMessage());
        return false;
    }
}

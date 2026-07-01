package com.showdown.springboot.orderfulfilmentservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component
public class OrderCaptureClient {

    private static final Logger log = LoggerFactory.getLogger(OrderCaptureClient.class);
    private static final String CB_NAME = "order-capture-service";

    private final WebClient webClient;

    public OrderCaptureClient(WebClient.Builder webClientBuilder,
                              @Value("${services.order-capture.url:http://sb-order-capture-service:8084}") String orderCaptureUrl) {
        this.webClient = webClientBuilder.baseUrl(orderCaptureUrl).build();
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "confirmOrderFallback")
    @Retry(name = CB_NAME)
    public boolean confirmOrder(String orderId) {
        try {
            Map<?, ?> response = webClient.post()
                    .uri("/api/orders/{id}/confirm", orderId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return response != null;
        } catch (WebClientResponseException e) {
            log.error("Error confirming order: {}", orderId, e);
            return false;
        }
    }

    @SuppressWarnings("unused")
    public boolean confirmOrderFallback(String orderId, Throwable t) {
        log.error("Order capture service unavailable for confirming order {} — circuit open. Reason: {}", orderId, t.getMessage());
        return false;
    }
}

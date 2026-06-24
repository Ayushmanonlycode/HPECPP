package com.showdown.springboot.ordercaptureservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

/**
 * HTTP client for the Inventory Service.
 *
 * <p>Protected by a Resilience4j circuit breaker and retry policy. If the
 * inventory service is unavailable, the circuit opens after 5 consecutive
 * failures and the fallback method is called, allowing order creation to
 * proceed with a "reserve later" strategy rather than hard-failing.
 *
 * <p>This is the correct pattern as per the Technology Implementation Map:
 * synchronous REST via Spring WebClient with fault tolerance via Resilience4j.
 */
@Component
public class InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);
    private static final String CB_NAME = "inventory-service";

    private final WebClient webClient;

    public InventoryClient(WebClient.Builder webClientBuilder,
                           @Value("${services.inventory.url:http://sb-inventory-service:8083}") String inventoryUrl) {
        this.webClient = webClientBuilder.baseUrl(inventoryUrl).build();
    }

    /**
     * Checks whether the requested quantity is available for a given item SKU.
     *
     * @param sku      Item SKU to check
     * @param quantity Requested quantity
     * @return {@code true} if stock is available; {@code false} on error or insufficient stock
     */
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "checkAvailabilityFallback")
    @Retry(name = CB_NAME)
    public boolean checkAvailability(String sku, int quantity) {
        try {
            Map<?, ?> response = webClient.get()
                    .uri("/api/inventory/{sku}", sku)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) return false;

            Number available = (Number) response.get("quantity");
            return available != null && available.intValue() >= quantity;
        } catch (WebClientResponseException.NotFound e) {
            log.warn("Inventory item not found for SKU: {}", sku);
            return false;
        }
    }

    /**
     * Reserves stock for a given item SKU.
     *
     * @param sku      Item SKU to reserve
     * @param quantity Requested quantity
     * @return {@code true} if reservation succeeded; {@code false} on error
     */
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "reserveStockFallback")
    @Retry(name = CB_NAME)
    public boolean reserveStock(String sku, int quantity) {
        try {
            Map<?, ?> response = webClient.post()
                    .uri("/api/inventory/{sku}/reserve", sku)
                    .bodyValue(Map.of("quantity", quantity))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response != null;
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                log.warn("Insufficient stock or invalid request for SKU: {}", sku);
            } else {
                log.error("Error reserving stock for SKU: {}", sku, e);
            }
            return false;
        }
    }

    /**
     * Fallback when the inventory service circuit is open or retries are exhausted.
     * Logs the failure and returns true to allow optimistic order creation.
     * A downstream fulfilment check will catch stock issues.
     */
    @SuppressWarnings("unused")
    public boolean checkAvailabilityFallback(String sku, int quantity, Throwable t) {
        log.error("Inventory service unavailable for SKU {} — circuit open. Reason: {}", sku, t.getMessage());
        // Optimistic fallback: allow the order through; inventory check will happen at fulfilment
        return true;
    }

    /**
     * Fallback for reserveStock when circuit is open.
     */
    @SuppressWarnings("unused")
    public boolean reserveStockFallback(String sku, int quantity, Throwable t) {
        log.error("Inventory service unavailable for reserving SKU {} — circuit open. Reason: {}", sku, t.getMessage());
        // Optimistic fallback: allow the order through; inventory check will happen at fulfilment
        return true;
    }
}

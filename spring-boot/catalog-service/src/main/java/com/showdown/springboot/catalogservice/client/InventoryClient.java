package com.showdown.springboot.catalogservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component
public class InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);

    private final WebClient webClient;

    public InventoryClient(WebClient.Builder webClientBuilder,
                           @Value("${services.inventory.url:http://localhost:8083}") String inventoryUrl) {
        this.webClient = webClientBuilder.baseUrl(inventoryUrl).build();
    }

    public Integer getAvailableQuantity(String sku) {
        try {
            Map<?, ?> response = webClient.get()
                    .uri("/api/inventory/{sku}", sku)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) return 0;

            Number available = (Number) response.get("availableQuantity");
            if (available != null) {
                return available.intValue();
            }
            
            Number total = (Number) response.get("quantity");
            Number reserved = (Number) response.get("reservedQuantity");
            if (total != null && reserved != null) {
                return total.intValue() - reserved.intValue();
            }

            return 0;
        } catch (WebClientResponseException.NotFound e) {
            log.warn("Inventory item not found for SKU: {}", sku);
            return 0;
        } catch (Exception e) {
            log.error("Error fetching inventory for SKU: {}", sku, e);
            return 0;
        }
    }
}

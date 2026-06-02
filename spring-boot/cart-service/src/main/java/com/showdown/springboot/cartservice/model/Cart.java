package com.showdown.springboot.cartservice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private Map<String, CartItem> items = new HashMap<>();
    private Instant createdAt;
    private Instant updatedAt;

    public Cart() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Cart(String userId) {
        this();
        this.userId = userId;
    }

    public BigDecimal getTotal() {
        return items.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getItemCount() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Map<String, CartItem> getItems() { return items; }
    public void setItems(Map<String, CartItem> items) { this.items = items; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

package com.showdown.springboot.inventoryservice.dto;

import java.time.Instant;
import java.util.UUID;

public class InventoryDto {

    private UUID id;
    private String itemSku;
    private int quantity;
    private int reservedQuantity;
    private int availableQuantity;
    private Instant lastUpdated;

    public InventoryDto() {
    }

    public InventoryDto(UUID id, String itemSku, int quantity, int reservedQuantity,
                        int availableQuantity, Instant lastUpdated) {
        this.id = id;
        this.itemSku = itemSku;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
        this.lastUpdated = lastUpdated;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getItemSku() { return itemSku; }
    public void setItemSku(String itemSku) { this.itemSku = itemSku; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
}

package com.showdown.springboot.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StockAdjustmentDto {

    @NotBlank(message = "Item SKU is required")
    private String itemSku;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    public StockAdjustmentDto() {
    }

    public StockAdjustmentDto(String itemSku, Integer quantity) {
        this.itemSku = itemSku;
        this.quantity = quantity;
    }

    public String getItemSku() { return itemSku; }
    public void setItemSku(String itemSku) { this.itemSku = itemSku; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}

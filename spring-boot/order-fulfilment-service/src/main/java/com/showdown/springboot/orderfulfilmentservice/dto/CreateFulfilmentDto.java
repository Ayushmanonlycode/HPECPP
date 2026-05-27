package com.showdown.springboot.orderfulfilmentservice.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CreateFulfilmentDto {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    public CreateFulfilmentDto() {
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
}

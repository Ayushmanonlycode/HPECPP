package com.showdown.springboot.orderfulfilmentservice.dto;

import jakarta.validation.constraints.NotNull;


public class CreateFulfilmentDto {

    @NotNull(message = "Order ID is required")
    private String orderId;

    public CreateFulfilmentDto() {
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}

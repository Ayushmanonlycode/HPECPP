package com.showdown.springboot.ordercaptureservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class CreateOrderDto {

    @NotNull(message = "User ID is required")
    private UUID userId;

    private String shippingAddress;

    @NotEmpty(message = "Order must have at least one line item")
    @Valid
    private List<OrderLineItemDto> lineItems;

    public CreateOrderDto() {
    }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<OrderLineItemDto> getLineItems() { return lineItems; }
    public void setLineItems(List<OrderLineItemDto> lineItems) { this.lineItems = lineItems; }
}

package com.showdown.springboot.ordercaptureservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class CreateOrderDto {

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Customer name is required")
    private String customerName;

    private String shippingAddress;

    @NotEmpty(message = "Order must have at least one line item")
    @Valid
    private List<OrderLineItemDto> lineItems;

    public CreateOrderDto() {
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<OrderLineItemDto> getLineItems() { return lineItems; }
    public void setLineItems(List<OrderLineItemDto> lineItems) { this.lineItems = lineItems; }
}

package com.showdown.springboot.catalogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


public class ItemDto {

    private String id;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "List price is required")
    @Positive(message = "List price must be positive")
    private BigDecimal listPrice;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    private String imageUrl;

    @NotNull(message = "Product ID is required")
    private String productId;

    private String productName;

    public ItemDto() {
    }

    public ItemDto(String id, String sku, BigDecimal listPrice, String description,
                   String imageUrl, String productId, String productName) {
        this.id = id;
        this.sku = sku;
        this.listPrice = listPrice;
        this.description = description;
        this.imageUrl = imageUrl;
        this.productId = productId;
        this.productName = productName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}


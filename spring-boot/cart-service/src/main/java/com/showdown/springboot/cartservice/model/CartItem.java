package com.showdown.springboot.cartservice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemSku;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;

    public CartItem() {
    }

    public CartItem(String itemSku, String productName, int quantity, BigDecimal unitPrice) {
        this.itemSku = itemSku;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public String getItemSku() { return itemSku; }
    public void setItemSku(String itemSku) { this.itemSku = itemSku; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}

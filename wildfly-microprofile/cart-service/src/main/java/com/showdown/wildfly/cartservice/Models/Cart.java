package com.showdown.wildfly.cartservice.Models;

import jakarta.persistence.Transient;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cart {

    @Id
    private String cartId;

    private String userId;

    private double totalAmount;

    //@OneToMany(mappedBy = "cart")
    @Transient
    private List<CartItem> items;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
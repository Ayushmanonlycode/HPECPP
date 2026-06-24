package com.showdown.springboot.cartservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDto {

    private String userId;
    private List<CartItemDto> items;
    private BigDecimal total;
    private int itemCount;

    public CartDto() {
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
}

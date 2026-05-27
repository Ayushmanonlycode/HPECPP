package com.showdown.springboot.cartservice.service;

import com.showdown.springboot.cartservice.dto.AddToCartDto;
import com.showdown.springboot.cartservice.dto.CartDto;

public interface CartService {

    CartDto getCart(String userId);

    CartDto addItem(String userId, AddToCartDto dto);

    CartDto updateItemQuantity(String userId, String sku, int quantity);

    CartDto removeItem(String userId, String sku);

    void clearCart(String userId);
}

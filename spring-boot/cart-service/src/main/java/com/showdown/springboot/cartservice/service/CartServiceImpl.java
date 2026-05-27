package com.showdown.springboot.cartservice.service;

import com.showdown.springboot.cartservice.dto.AddToCartDto;
import com.showdown.springboot.cartservice.dto.CartDto;
import com.showdown.springboot.cartservice.dto.CartItemDto;
import com.showdown.springboot.cartservice.model.Cart;
import com.showdown.springboot.cartservice.model.CartItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final String CART_KEY_PREFIX = "cart:";

    private final RedisTemplate<String, Cart> cartRedisTemplate;

    @Value("${cart.ttl.hours:24}")
    private long cartTtlHours;

    public CartServiceImpl(RedisTemplate<String, Cart> cartRedisTemplate) {
        this.cartRedisTemplate = cartRedisTemplate;
    }

    @Override
    public CartDto getCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        return toDto(cart);
    }

    @Override
    public CartDto addItem(String userId, AddToCartDto dto) {
        Cart cart = getOrCreateCart(userId);

        CartItem existing = cart.getItems().get(dto.getItemSku());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + dto.getQuantity());
        } else {
            CartItem newItem = new CartItem(dto.getItemSku(), dto.getProductName(),
                    dto.getQuantity(), dto.getUnitPrice());
            cart.getItems().put(dto.getItemSku(), newItem);
        }

        cart.setUpdatedAt(Instant.now());
        saveCart(userId, cart);
        return toDto(cart);
    }

    @Override
    public CartDto updateItemQuantity(String userId, String sku, int quantity) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cart.getItems().get(sku);
        if (item == null) {
            throw new IllegalArgumentException("Item with SKU '" + sku + "' not found in cart");
        }

        if (quantity <= 0) {
            cart.getItems().remove(sku);
        } else {
            item.setQuantity(quantity);
        }

        cart.setUpdatedAt(Instant.now());
        saveCart(userId, cart);
        return toDto(cart);
    }

    @Override
    public CartDto removeItem(String userId, String sku) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().remove(sku);
        cart.setUpdatedAt(Instant.now());
        saveCart(userId, cart);
        return toDto(cart);
    }

    @Override
    public void clearCart(String userId) {
        cartRedisTemplate.delete(cartKey(userId));
    }

    // ── Helpers ──────────────────────────────────────────────────

    private Cart getOrCreateCart(String userId) {
        Cart cart = cartRedisTemplate.opsForValue().get(cartKey(userId));
        if (cart == null) {
            cart = new Cart(userId);
        }
        return cart;
    }

    private void saveCart(String userId, Cart cart) {
        cartRedisTemplate.opsForValue().set(cartKey(userId), cart, cartTtlHours, TimeUnit.HOURS);
    }

    private String cartKey(String userId) {
        return CART_KEY_PREFIX + userId;
    }

    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setUserId(cart.getUserId());
        dto.setTotal(cart.getTotal());
        dto.setItemCount(cart.getItemCount());
        dto.setItems(cart.getItems().values().stream()
                .map(this::toItemDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private CartItemDto toItemDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setItemSku(item.getItemSku());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}

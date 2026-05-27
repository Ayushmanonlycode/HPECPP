package com.showdown.springboot.cartservice.controller;

import com.showdown.springboot.cartservice.dto.AddToCartDto;
import com.showdown.springboot.cartservice.dto.CartDto;
import com.showdown.springboot.cartservice.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDto> addItem(@PathVariable String userId,
                                           @Valid @RequestBody AddToCartDto dto) {
        return ResponseEntity.ok(cartService.addItem(userId, dto));
    }

    @PutMapping("/{userId}/items/{sku}")
    public ResponseEntity<CartDto> updateItemQuantity(@PathVariable String userId,
                                                      @PathVariable String sku,
                                                      @RequestBody Map<String, Integer> body) {
        int quantity = body.getOrDefault("quantity", 0);
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, sku, quantity));
    }

    @DeleteMapping("/{userId}/items/{sku}")
    public ResponseEntity<CartDto> removeItem(@PathVariable String userId,
                                               @PathVariable String sku) {
        return ResponseEntity.ok(cartService.removeItem(userId, sku));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}

package com.showdown.springboot.cartservice.controller;

import com.showdown.springboot.cartservice.dto.AddToCartDto;
import com.showdown.springboot.cartservice.dto.CartDto;
import com.showdown.springboot.cartservice.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Redis-backed shopping cart. Requires a valid JWT Bearer token.")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get cart for a user", description = "Returns the current cart items from Redis. Returns an empty cart if none exists.")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/items")
    @Operation(summary = "Add item to cart", description = "Adds a product to the user's cart. TTL is 24 hours.")
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

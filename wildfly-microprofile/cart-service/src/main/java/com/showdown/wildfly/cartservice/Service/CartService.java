package com.showdown.wildfly.cartservice.Service;

import com.showdown.wildfly.cartservice.Models.Cart;
import com.showdown.wildfly.cartservice.Models.CartItem;
import com.showdown.wildfly.cartservice.Repository.CartRepository;

import com.showdown.wildfly.cartservice.Models.DTO.CartResponse;
import com.showdown.wildfly.cartservice.Models.DTO.CartItemResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CartService {

    @Inject
    CartRepository repository;

    public Cart getCart(String userId) {
        return repository.getCartByUserId(userId);
    }

    public void createCart(Cart cart) {
        repository.addCart(cart);
    }

    public void addItem(CartItem item) {
        repository.addItem(item);
    }

    public CartItem updateQuantity(String sku, int quantity) {

        CartItem item = repository.getItemBySku(sku);

        if (item != null) {
            item.setQuantity(quantity);
            repository.updateItem(item);
        }

        return item;
    }

    public void removeItem(String sku) {
        repository.removeItem(sku);
    }

    public void clearCart(String userId) {
        repository.clearCart(userId);
    }
    public CartResponse buildCartResponse(Cart cart) {

        if (cart == null) {
            return null;
        }

        CartResponse response = new CartResponse();

        response.setUserId(cart.getUserId());
        response.setTotal(cart.getTotalAmount());

        ArrayList<CartItemResponse> items = new ArrayList<>();

        int itemCount = 0;

        if (cart.getItems() != null) {

            for (CartItem item : cart.getItems()) {

                CartItemResponse dto = new CartItemResponse();

                dto.setItemSku(item.getSku());
                dto.setProductName(item.getProductName());
                dto.setQuantity(item.getQuantity());
                dto.setUnitPrice(item.getPrice());
                dto.setSubtotal(
                    item.getPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())));

                itemCount += item.getQuantity();

                items.add(dto);
            }
        }

        response.setItems(items);
        response.setItemCount(itemCount);

        return response;
    }
}
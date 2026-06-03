package com.showdown.wildfly.cartservice.Service;

import com.showdown.wildfly.cartservice.Models.Cart;
import com.showdown.wildfly.cartservice.Models.CartItem;
import com.showdown.wildfly.cartservice.Repository.CartRepository;
import com.showdown.wildfly.cartservice.Models.Cart;
import com.showdown.wildfly.cartservice.Models.CartItem;

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

        if(item != null){
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
}

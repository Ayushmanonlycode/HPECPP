package com.showdown.wildfly.cartservice.Repository;

import com.showdown.wildfly.cartservice.Models.Cart;
import com.showdown.wildfly.cartservice.Models.CartItem;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class CartRepository {

    @PersistenceContext(unitName = "cartPU")
    EntityManager em;
    public Cart getCartByUserId(String userId) {

        try {
            return em.createQuery(
                    "SELECT c FROM Cart c WHERE c.userId = :userId",
                    Cart.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }
    public void addCart(Cart cart) {
        em.persist(cart);
    }
    public void addItem(CartItem item) {
        em.persist(item);
    }
    public CartItem getItemBySku(String sku) {

        try {
            return em.createQuery(
                    "SELECT i FROM CartItem i WHERE i.sku = :sku",
                    CartItem.class)
                    .setParameter("sku", sku)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }
    public CartItem updateItem(CartItem item) {
        return em.merge(item);
    }
    public void removeItem(String sku) {

        CartItem item = getItemBySku(sku);

        if(item != null){
            em.remove(em.contains(item) ? item : em.merge(item));
        }
    }
    public void clearCart(String userId) {

        Cart cart = getCartByUserId(userId);

        if(cart != null){

            em.createQuery(
                    "DELETE FROM CartItem i WHERE i.cart.cartId = :cartId")
                    .setParameter("cartId", cart.getCartId())
                    .executeUpdate();
        }
    }
}

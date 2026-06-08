package com.showdown.wildfly.cartservice.Repository;

import com.showdown.wildfly.cartservice.Models.Cart;
import com.showdown.wildfly.cartservice.Models.CartItem;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class CartRepository {

    private static final Logger log = Logger.getLogger(CartRepository.class.getName());

    @Inject
    @ConfigProperty(name = "redis.sentinel.hosts")
    String sentinelHosts; // "host1:26379,host2:26379,host3:26379"

    @Inject
    @ConfigProperty(name = "redis.master.name", defaultValue = "mymaster")
    String masterName;

    private JedisSentinelPool pool;

    private final Jsonb jsonb = JsonbBuilder.create();

    @PostConstruct
    void init() {
        Set<String> sentinels = new HashSet<>(Arrays.asList(sentinelHosts.split(",")));
        pool = new JedisSentinelPool(masterName, sentinels);
        log.info("JedisSentinelPool initialised — master: " + masterName + ", sentinels: " + sentinels);
    }

    @PreDestroy
    void destroy() {
        if (pool != null && !pool.isClosed()) pool.close();
    }

    // ─────────────────────────────────────────────────────────────

    private String cartKey(String userId) {
        return "cart:" + userId;
    }

    public Cart getCartByUserId(String userId) {
        try (Jedis jedis = pool.getResource()) {
            String json = jedis.get(cartKey(userId));
            if (json == null) return null;
            return jsonb.fromJson(json, Cart.class);
        } catch (JedisException e) {
            log.severe("Redis error in getCartByUserId: " + e.getMessage());
            return null;
        }
    }

    public void addCart(Cart cart) {
        if (cart.getItems() == null) cart.setItems(new ArrayList<>());
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(cartKey(cart.getUserId()), 86400, jsonb.toJson(cart));
        } catch (JedisException e) {
            log.severe("Redis error in addCart: " + e.getMessage());
        }
    }

    public void addItem(CartItem item) {
        Cart cart = getCartByUserId(item.getCart().getUserId());
        if (cart == null) return;
        if (cart.getItems() == null) cart.setItems(new ArrayList<>());
        cart.getItems().add(item);
        addCart(cart);
    }

    public CartItem getItemBySku(String sku) {
        try (Jedis jedis = pool.getResource()) {
            Set<String> keys = jedis.keys("cart:*");
            for (String key : keys) {
                String json = jedis.get(key);
                if (json == null) continue;
                Cart cart = jsonb.fromJson(json, Cart.class);
                if (cart.getItems() == null) continue;
                for (CartItem item : cart.getItems()) {
                    if (item.getSku().equals(sku)) return item;
                }
            }
        } catch (JedisException e) {
            log.severe("Redis error in getItemBySku: " + e.getMessage());
        }
        return null;
    }

    public CartItem updateItem(CartItem item) {
        Cart cart = getCartByUserId(item.getCart().getUserId());
        if (cart == null || cart.getItems() == null) return null;
        for (int i = 0; i < cart.getItems().size(); i++) {
            if (cart.getItems().get(i).getSku().equals(item.getSku())) {
                cart.getItems().set(i, item);
                addCart(cart);
                return item;
            }
        }
        return null;
    }

    public void removeItem(String sku) {
        try (Jedis jedis = pool.getResource()) {
            Set<String> keys = jedis.keys("cart:*");
            for (String key : keys) {
                String json = jedis.get(key);
                if (json == null) continue;
                Cart cart = jsonb.fromJson(json, Cart.class);
                if (cart.getItems() == null) continue;
                cart.getItems().removeIf(i -> i.getSku().equals(sku));
                addCart(cart);
            }
        } catch (JedisException e) {
            log.severe("Redis error in removeItem: " + e.getMessage());
        }
    }

    public void clearCart(String userId) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(cartKey(userId));
        } catch (JedisException e) {
            log.severe("Redis error in clearCart: " + e.getMessage());
        }
    }
}
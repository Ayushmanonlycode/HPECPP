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
    import redis.clients.jedis.JedisPool;
    import redis.clients.jedis.JedisSentinelPool;
    import redis.clients.jedis.exceptions.JedisException;
    import redis.clients.jedis.util.Pool;

    import java.math.BigDecimal;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.HashSet;
    import java.util.Set;

    import java.util.logging.Logger;

    @ApplicationScoped
    public class CartRepository {

        private static final Logger log = Logger.getLogger(CartRepository.class.getName());

        @Inject
        @ConfigProperty(name = "redis.host")
        String redisHost;

        @Inject
        @ConfigProperty(name = "redis.port")
        int redisPort;


        @Inject
        @ConfigProperty(name = "redis.sentinel.hosts", defaultValue = "")
        String sentinelHosts;

        @Inject
        @ConfigProperty(name = "redis.master.name", defaultValue = "mymaster")
        String masterName;


        private Pool<Jedis> pool;
        private final Jsonb jsonb = JsonbBuilder.create();

        @PostConstruct
        void init() {
            if (sentinelHosts != null && !sentinelHosts.isBlank()) {
                Set<String> sentinels = new HashSet<>(Arrays.asList(sentinelHosts.split(",")));
                sentinels.removeIf(String::isBlank);

                pool = new JedisSentinelPool(masterName, sentinels);
                log.info("Connected to Redis via Sentinel (master=" + masterName
                        + ", sentinels=" + sentinels + ")");
            } else {
                pool = new JedisPool(redisHost, redisPort);
                log.info("Connected to Redis directly at " + redisHost + ":" + redisPort
                        + " (no sentinel hosts configured)");
            }
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

            if (cart.getItems() == null)
                cart.setItems(new ArrayList<>());

            cart.setUpdatedAt(java.time.Instant.now());

            try (Jedis jedis = pool.getResource()) {
                jedis.setex(cartKey(cart.getUserId()), 86400, jsonb.toJson(cart));
            } catch (JedisException e) {
                log.severe("Redis error in addCart: " + e.getMessage());
            }
        }

        public void addItem(String userId, CartItem item) {

            Cart cart = getCartByUserId(userId);

            if (cart == null) {

               cart = new Cart();
                cart.setUserId(userId);
                cart.setItems(new ArrayList<>());
                cart.setTotalAmount(BigDecimal.ZERO);

                cart.setCreatedAt(java.time.Instant.now());
                cart.setUpdatedAt(java.time.Instant.now());
            }

            if (cart.getItems() == null)
                cart.setItems(new ArrayList<>());

            for (CartItem existing : cart.getItems()) {

                if (existing.getSku().equals(item.getSku())) {

                    existing.setQuantity(
                        existing.getQuantity() + item.getQuantity()
                    );

                    recalculateTotal(cart);

                    addCart(cart);

                    return;
                }
            }

            cart.getItems().add(item);

            recalculateTotal(cart);

            addCart(cart);
        }

        public CartItem getItemBySku(String userId, String sku) {

            Cart cart = getCartByUserId(userId);

            if (cart == null || cart.getItems() == null)
                return null;

            for (CartItem item : cart.getItems()) {

                if (item.getSku().equals(sku))
                    return item;
            }

            return null;
        }

        public CartItem updateItem(String userId, CartItem item) {

            Cart cart = getCartByUserId(userId);

            if (cart == null || cart.getItems() == null)
                return null;

            for (int i = 0; i < cart.getItems().size(); i++) {

                if (cart.getItems().get(i).getSku().equals(item.getSku())) {

                    cart.getItems().set(i, item);

                    recalculateTotal(cart);

                    addCart(cart);

                    return item;
                }
            }

            return null;
        }

        public void removeItem(String userId, String sku) {

            Cart cart = getCartByUserId(userId);

            if (cart == null || cart.getItems() == null)
                return;

            cart.getItems().removeIf(i -> i.getSku().equals(sku));

            recalculateTotal(cart);

            addCart(cart);
        }

        public void clearCart(String userId) {
            try (Jedis jedis = pool.getResource()) {
                jedis.del(cartKey(userId));
            } catch (JedisException e) {
                log.severe("Redis error in clearCart: " + e.getMessage());
            }
        }
        private void recalculateTotal(Cart cart) {

            BigDecimal total = BigDecimal.ZERO;

            if (cart.getItems() != null) {
                for (CartItem item : cart.getItems()) {

                    if (item.getPrice() != null) {

                        total = total.add(
                            item.getPrice().multiply(
                                BigDecimal.valueOf(item.getQuantity())
                            )
                        );
                    }
                }
            }

            cart.setTotalAmount(total);
        }
    }
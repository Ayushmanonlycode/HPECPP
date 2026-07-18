# Cart Service — Spring Boot

The **Cart Service** manages customer shopping carts using a high-performance **Redis** cache for rapid read/write operations during browsing sessions.

---

## Technology Stack

- **Core Framework**: Spring Boot 3 (Java 21)
- **Caching Database**: Redis 7 (backed by `sb-cart-db` container)
- **Data Access**: Spring Data Redis (`RedisTemplate` with JSON serialization)
- **Security**: Spring Security OAuth2 Resource Server (Keycloak public key validation)

---

## Data Model

Cart data is stored as serialized JSON in Redis rather than a relational database. The object model consists of two classes:

### Cart
| Field | Type | Description |
|---|---|---|
| `userId` | `String` | Owner identifier (guest token or registered username). |
| `items` | `Map<String, CartItem>` | Collection of items, keyed by SKU. |
| `createdAt` | `Instant` | Cart creation timestamp. |
| `updatedAt` | `Instant` | Last modification timestamp. |

Computed properties: `getTotal()` (sum of all item subtotals), `getItemCount()` (sum of all item quantities).

### CartItem
| Field | Type | Description |
|---|---|---|
| `itemSku` | `String` | SKU identifier from the Catalog Service. |
| `productName` | `String` | Human-readable product name. |
| `quantity` | `int` | Number of units. |
| `unitPrice` | `BigDecimal` | Price per unit at the time of addition. |

Computed property: `getSubtotal()` returns `unitPrice * quantity`.

### Redis Key Pattern and TTL

- **Key**: `cart:{userId}`
- **TTL**: 24 hours. Every modification (add, update, remove) resets the TTL, ensuring active sessions are not expired prematurely.

---

## Security Configuration

To support guest shopping sessions before login, all `/api/cart/**` routes are configured with `.permitAll()` in the Spring Security filter chain. The frontend generates a unique temporary token for anonymous visitors and sends it as the `{userId}` path variable.

---

## API Endpoints

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/cart/{userId}` | Retrieve the current cart. Returns an empty cart if none exists. |
| `POST` | `/api/cart/{userId}/items` | Add a product SKU to the cart. Body: `AddToCartDto`. |
| `PUT` | `/api/cart/{userId}/items/{sku}` | Update quantity of a SKU. Body: `{"quantity": 5}`. |
| `DELETE` | `/api/cart/{userId}/items/{sku}` | Remove a specific SKU from the cart. |
| `DELETE` | `/api/cart/{userId}` | Clear all cart contents (typically after checkout). |

---

## Environment Variables

| Property | Environment Variable | Default | Description |
|---|---|---|---|
| `spring.data.redis.host` | `SPRING_DATA_REDIS_HOST` | `localhost` | Redis server address. |
| `spring.data.redis.port` | `SPRING_DATA_REDIS_PORT` | `6379` | Redis server port. |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `KEYCLOAK_URL` | `http://localhost:8180/realms/jpetstore` | Keycloak issuer URL. |

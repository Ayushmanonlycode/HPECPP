# Catalog Service — Spring Boot

The **Catalog Service** manages the product catalog of the JPetStore, exposing APIs to query Categories, Products, and Items (specific SKUs).

---

## Technology Stack

- **Core Framework**: Spring Boot 3 (Java 21)
- **Database Access**: Spring Data JPA & Hibernate
- **Database**: PostgreSQL (backed by `sb-catalog-db` container)
- **Security**: Spring Security OAuth2 Resource Server (Keycloak public key validation)
- **Inter-service client**: Spring Webflux WebClient (with Client Credentials token exchange)

---

## Database Schema

The catalog database consists of three tables mapping to entities:

### 1. `categories` Table
- `id` (VARCHAR/UUID, Primary Key)
- `name` (VARCHAR, Not Null)
- `description` (VARCHAR)

### 2. `products` Table
- `id` (VARCHAR/UUID, Primary Key)
- `name` (VARCHAR, Not Null)
- `description` (VARCHAR, 1000)
- `species` (VARCHAR)
- `category_id` (VARCHAR, Foreign Key referencing `categories(id)`)

### 3. `items` Table
- `id` (VARCHAR/UUID, Primary Key)
- `sku` (VARCHAR, Unique, Not Null)
- `list_price` (NUMERIC(10,2), Not Null)
- `description` (VARCHAR, 500)
- `image_url` (VARCHAR)
- `product_id` (VARCHAR, Foreign Key referencing `products(id)`)

---

## Security & Token Validation

1. **Inbound HTTP Requests**:
   - GET requests to public paths (`/api/categories/**`, `/api/products/**`, `/api/items/**`) are open without authorization.
   - Write actions (POST, PUT, DELETE) require a Bearer token containing the required scopes, validated against Keycloak.
2. **Outbound Inter-service Requests**:
   - To serve catalog items, the service makes an outbound HTTP request to the **Inventory Service** to fetch item stock quantities.
   - In `WebClientConfig`, an `AuthorizedClientServiceOAuth2AuthorizedClientManager` automatically attaches an OAuth2 Client Credentials token (acquired from Keycloak under client `jpetstore-gateway`) to the request headers.

---

## API Endpoints

### Category APIs
- `GET /api/categories`: List all categories.
- `GET /api/categories/{id}`: Get category by ID.
- `POST /api/categories`: Create a category (Secure).
- `PUT /api/categories/{id}`: Update a category (Secure).
- `DELETE /api/categories/{id}`: Delete a category (Secure).

### Product APIs
- `GET /api/products`: List all products (supports optional `categoryId` filter).
- `GET /api/products/{id}`: Get product by ID.
- `GET /api/products/search?q={query}`: Search products.
- `POST /api/products`: Create a product (Secure).
- `PUT /api/products/{id}`: Update a product (Secure).
- `DELETE /api/products/{id}`: Delete a product (Secure).

### Item (SKU) APIs
- `GET /api/items`: List all items (supports optional `productId` filter).
- `GET /api/items/{id}`: Get item by ID.
- `GET /api/items/sku/{sku}`: Get item by SKU.
- `POST /api/items`: Create an item (Secure).
- `PUT /api/items/{id}`: Update an item (Secure).
- `DELETE /api/items/{id}`: Delete an item (Secure).

---

## Environment Variables & Configuration

Declared in `src/main/resources/application.properties`:

| Property Path | Environment Variable | Default Value | Description |
|---|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://sb-catalog-db:5432/catalogdb` | JDBC connection URL. |
| `services.inventory.url` | `SERVICES_INVENTORY_URL` | `http://localhost:8083` | Host URL for the downstream inventory service. |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `KEYCLOAK_URL` | `http://localhost:8180/realms/jpetstore` | Keycloak server URL for resource validation. |
| `spring.security.oauth2.client.registration.keycloak.client-secret` | `KEYCLOAK_CLIENT_SECRET` | (None) | OAuth2 Client Secret for fetching client credentials token. |

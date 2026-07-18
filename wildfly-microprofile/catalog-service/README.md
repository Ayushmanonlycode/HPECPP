# Catalog Service — WildFly MicroProfile

The **Catalog Service** manages the product catalog for the WildFly stack, exposing JAX-RS endpoints for categories, products, and items (SKUs). Unlike the Spring Boot equivalent, this service operates as a **standalone CRUD service** with no downstream integration to the Inventory Service.

---

## Technology Stack

- **Runtime**: WildFly Application Server (latest)
- **Language**: Java 17
- **Framework**: Jakarta EE 10, MicroProfile 6.1
- **Database Access**: Jakarta Persistence (JPA) with Hibernate
- **Database**: PostgreSQL 16 (backed by `wf-catalog-db` container)
- **Packaging**: WAR (`catalog-service.war`)

---

## Database Schema

### `Category` Entity
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `VARCHAR` | Primary Key | Category identifier. |
| `category_name` | `VARCHAR` | — | Display name of the category. |

### `Product` Entity
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `VARCHAR` | Primary Key | Product identifier. |
| `name` | `VARCHAR` | — | Product name. |
| `description` | `VARCHAR` | — | Product description. |
| `species` | `VARCHAR` | — | Animal species classification. |
| `category_id` | `VARCHAR` | Foreign Key to `Category` | Parent category reference. |

### `Item` Entity
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `VARCHAR` | Primary Key | Item identifier. |
| `sku` | `VARCHAR` | Unique | Stock-keeping unit code. |
| `price` | `NUMERIC` | — | List price. |
| `description` | `VARCHAR` | — | Item description. |
| `product_id` | `VARCHAR` | Foreign Key to `Product` | Parent product reference. |

---

## Key Architectural Difference from Spring Boot

The Spring Boot Catalog Service makes a synchronous HTTP call to the Inventory Service (`InventoryClient`) to enrich each item response with available stock quantities. The WildFly Catalog Service does **not** perform this enrichment — it returns catalog data only, with no stock availability information.

---

## API Endpoints

All endpoints are exposed under the WAR context root `/catalog-service`.

### Categories
| Method | Path | Description |
|---|---|---|
| `GET` | `/categories` | List all categories. |
| `GET` | `/categories/{id}` | Get category by ID. |
| `POST` | `/categories/add` | Create a new category. |

### Products
| Method | Path | Description |
|---|---|---|
| `GET` | `/products` | List all products (supports `?categoryId=` filter). |
| `GET` | `/products/{id}` | Get product by ID. |
| `POST` | `/products/add` | Create a new product. |

### Items
| Method | Path | Description |
|---|---|---|
| `GET` | `/items` | List all items (supports `?productId=` filter). |
| `GET` | `/items/{id}` | Get item by ID. |
| `POST` | `/items/add` | Create a new item. |

---

## Security

No security validation is implemented. All endpoints are publicly accessible without authentication.

---

## Datasource Configuration

The PostgreSQL datasource is registered at build time in the `Dockerfile`:

```
data-source add --name=CatalogDS --jndi-name=java:/CatalogDSP \
  --connection-url=jdbc:postgresql://wf-catalog-db:5432/catalogdb \
  --user-name=showdown --password=showdown123
```

Credentials are hardcoded and do not reference environment variables from `docker-compose.yml`.

---

## Docker Configuration

| Property | Value |
|---|---|
| Container Name | `wf-catalog-service` |
| Internal Port | `8080` |
| Mapped Host Port | `9082` |
| Database Container | `wf-catalog-db` |
| Database Name | `catalogdb` |

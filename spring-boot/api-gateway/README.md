# API Gateway — Spring Cloud Gateway

The **API Gateway** is a Java-based, reactive reverse proxy built using **Spring Cloud Gateway** and **Spring WebFlux**. It is designed to act as the single entry point and trust boundary for all client requests entering the Spring Boot microservices cluster.

> [!NOTE]
> **Active Production Router**: In the provided Docker Compose configuration (`docker-compose.yml`), the active gateway on port `8080` is an **NGINX container** (`sb-api-gateway`) configured via `spring-boot/nginx/nginx.conf`. This Java Spring Cloud Gateway module is a alternative boundary implementation that can be run standalone on port `8080`.

---

## Technology Stack

- **Core Framework**: Spring Boot 3 (Java 21)
- **Routing Engine**: Spring Cloud Gateway (Reactive)
- **Web Runtime**: Spring WebFlux (Netty-backed reactive engine)
- **Security**: Spring Security OAuth2 Resource Server (JWT validation via Keycloak)
- **Documentation**: Springdoc OpenAPI WebFlux UI

---

## Route Mappings & Downstream Routing

The gateway maps incoming paths `/api/*` to the following microservice targets:

| Route ID | Inbound Path | Target Service | Default URL |
|---|---|---|---|
| `user-service` | `/api/users/**` | `sb-user-service` | `http://localhost:8081` |
| `catalog-service-categories` | `/api/categories/**` | `sb-catalog-service` | `http://localhost:8082` |
| `catalog-service-products` | `/api/products/**` | `sb-catalog-service` | `http://localhost:8082` |
| `catalog-service-items` | `/api/items/**` | `sb-catalog-service` | `http://localhost:8082` |
| `inventory-service` | `/api/inventory/**` | `sb-inventory-service` | `http://localhost:8083` |
| `order-capture-service` | `/api/orders/**` | `sb-order-capture-service` | `http://localhost:8084` |
| `order-fulfilment-service` | `/api/fulfilments/**` | `sb-order-fulfilment-service` | `http://localhost:8085` |
| `cart-service` | `/api/cart/**` | `sb-cart-service` | `http://localhost:8086` |

---

## Security Topology & JWT Boundary

When active, this gateway is designed to serve as the **security gatekeeper**:
1. **Stateless Sessions**: Configured via `.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())` for a pure REST architecture.
2. **Public Exchanges**:
   - `/api/users/register` and `/api/users/login` (POST requests) are open.
   - Catalog browsing GET endpoints (`/api/categories/**`, `/api/products/**`, `/api/items/**`) are open.
   - `/actuator/**` health and metrics endpoints are permitted.
3. **JWT Resource Server Verification**:
   - For all other secure endpoints, the gateway intercepts the inbound `Authorization: Bearer <JWT>` header and validates it against the Keycloak realm JWKS endpoint at `${KEYCLOAK_URL}/realms/jpetstore`.
   - On successful validation, the JWT token is forwarded down to the corresponding microservice.

---

## Environment Variables & Configuration

The configuration parameters are stored in `src/main/resources/application.yml`. Important keys include:

| Property Path | Environment Variable | Default Value | Description |
|---|---|---|---|
| `server.port` | `PORT` | `8080` | Local port the reactive server binds to. |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | `KEYCLOAK_URL` | `http://localhost:8180/realms/jpetstore` | Keycloak issuer endpoint used for token signature checks. |
| `spring.cloud.gateway.routes[x].uri` | `GATEWAY_*_SERVICE` | `http://localhost:808*` | Dynamic target URLs for microservice routing. |

---

## Observability & Diagnostics

- **Health Checks**: The gateway exposes Liveness and Readiness probes at `/actuator/health`.
- **Prometheus Metrics**: Exposes micrometer metrics at `/actuator/prometheus` (which scrapes request counts, route latency, and connection pools).

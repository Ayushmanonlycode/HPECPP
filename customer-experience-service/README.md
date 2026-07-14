# Customer Experience Service (React Frontend)

The **Customer Experience Service** is a modern, responsive single-page web application (SPA) acting as the graphical storefront for the microservices showdown. It allows users to browse pet categories, view products, add items to a shopping cart, register/login, manage profiles, check out, and view their order history.

---

## Technology Stack

- **Core Framework**: React 19 (TypeScript)
- **Build Tool**: Vite 8 (Hot Module Replacement)
- **Routing**: React Router DOM (v7)
- **HTTP Client**: Axios (with custom interceptors for session and security headers)
- **Styling**: Pure CSS (using modular stylesheets per component/page)

---

## Directory Structure

```
customer-experience-service/
├── src/
│   ├── components/       # Reusable components (Navbar, ProductCard, SearchBar, SkeletonCard, Spinner, etc.)
│   ├── context/          # Global Context Providers (AuthContext, ToastContext)
│   ├── layouts/          # Page layouts (MainLayout, AuthLayout)
│   ├── pages/            # View pages (HomePage, CartPage, CheckoutPage, ProfilePage, etc.)
│   ├── services/         # API clients (api.ts, catalogApi.ts, cartApi.ts, orderApi.ts, userApi.ts)
│   ├── styles/           # Global styles and design tokens
│   ├── types/            # TypeScript type declarations for API models
│   ├── App.tsx           # Router structure and main entry point
│   ├── index.css         # Baseline style definitions
│   └── main.tsx          # React application root mount
├── vite.config.ts        # Vite configuration (including proxy setup)
└── package.json          # Node dependencies and scripts
```

---

## Switch Backend Stacks (Spring Boot vs. WildFly)

The React frontend accesses the backend via a single `/api` proxy. Because Vite's development server is configured to proxy all `/api` traffic, switching between Stack A (Spring Boot) and Stack B (WildFly) requires editing `vite.config.ts`:

1. Open `customer-experience-service/vite.config.ts`.
2. Locate the `server.proxy` block:
   ```typescript
   proxy: {
     '/api': {
       target: 'http://localhost:8080', // Switch to 'http://localhost:9080' for WildFly
       changeOrigin: true,
       secure: false,
     },
   }
   ```
3. Change the `target` port:
   - **Spring Boot**: `http://localhost:8080` (NGINX gateway)
   - **WildFly**: `http://localhost:9080` (NGINX gateway / Local host proxy target)
4. Restart the Vite development server (`npm run dev`).

---

## Authentication & Session Header Flow

The frontend does not communicate directly with Keycloak. Instead, it utilizes two headers injected by Axios request interceptors (`src/services/api.ts`):

1. **User Identifier (`X-User-Id`)**:
   - Pulled from `localStorage.getItem('userId')` if present (can be a guest ID or logged-in username).
   - Injected into all requests to associate the session with the shopping cart and checkout processes.
2. **Bearer Token (`Authorization: Bearer <token>`)**:
   - Generated dynamically when logging in or registering via the User Service.
   - Attached to request headers for secured actions (e.g., retrieving profile data, listing secure orders).

---

## Page Routes

| Route Path | Associated Page | Protected? | Description |
|---|---|---|---|
| `/` | `HomePage` | No | Carousel of categories, search bar, and featured items. |
| `/category/:id` | `ProductListingPage`| No | Displays all products under a selected category. |
| `/product/:id` | `ProductDetailPage` | No | Lists specific items (SKUs) for a product, showing stock status. |
| `/cart` | `CartPage` | No | Shopping cart details (backed by Redis/database). |
| `/checkout` | `CheckoutPage` | No | Form to enter shipping details and place an order (permits guest checkout). |
| `/order-confirmation`| `OrderConfirmationPage`| No | Success page showing confirmation details and order ID. |
| `/auth` | `AuthPage` | No | Single page handling Register and Login toggles. |
| `/profile` | `ProfilePage` | **Yes** | Shows authenticated user information. |
| `/orders` | `OrderHistoryPage` | **Yes** | Displays a historical log of placed orders. |

---

## Running the App Locally

### 1. Install Dependencies
```bash
npm install
```

### 2. Launch Development Server
```bash
npm run dev
```
The app will start at `http://localhost:5173`. Make sure the targeted backend stack is active and healthy in Docker.

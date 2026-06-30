// ── Catalog ───────────────────────────────────────────────

export interface Category {
  id: string;
  name: string;
  description: string | null;
}

export interface Product {
  id: string;
  name: string;
  description: string | null;
  species: string | null;
  categoryId: string;
  categoryName: string;
}

export interface Item {
  id: string;
  sku: string;
  listPrice: number;
  description: string | null;
  imageUrl: string | null;
  productId: string;
  productName: string;
  availableQuantity?: number;
}

// ── Cart ──────────────────────────────────────────────────

export interface CartItem {
  itemSku: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface Cart {
  userId: string;
  items: CartItem[];
  total: number;
  itemCount: number;
}

export interface AddToCartRequest {
  itemSku: string;
  productName: string;
  quantity: number;
  unitPrice: number;
}

// ── Orders ────────────────────────────────────────────────

export interface OrderLineItem {
  itemSku: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
}

export interface Order {
  id: string;
  userId: string;
  customerName: string;
  status: 'CREATED' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  totalAmount: number;
  shippingAddress: string;
  createdAt: string;
  updatedAt: string;
  lineItems: OrderLineItem[];
}

export interface CreateOrderRequest {
  userId: string;
  customerName: string;
  shippingAddress: string;
  lineItems: {
    itemSku: string;
    productName: string;
    quantity: number;
    unitPrice: number;
  }[];
}

// ── Users ─────────────────────────────────────────────────

export interface UserProfile {
  id: string;
  username: string;
  email: string;
  firstName: string | null;
  lastName: string | null;
  phone: string | null;
  address: string | null;
  city: string | null;
  state: string | null;
  zip: string | null;
  country: string | null;
  status: string;
  createdAt: string;
  updatedAt: string;
}

export interface UserProfileUpdateDto {
  firstName?: string;
  lastName?: string;
  phone?: string;
  address?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
  password: string;
}

// ── API Error ─────────────────────────────────────────────

export interface ApiError {
  message: string;
  status: number;
}

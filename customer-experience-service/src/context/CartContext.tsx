import { createContext, useContext, useState, useEffect, useCallback, type ReactNode } from 'react';
import type { Cart, AddToCartRequest } from '../types/api';
import { cartApi } from '../services/cartApi';
import { useAuth } from './AuthContext';

interface CartState {
  cart: Cart | null;
  itemCount: number;
  loading: boolean;
  addItem: (item: AddToCartRequest) => Promise<void>;
  removeItem: (sku: string) => Promise<void>;
  updateQuantity: (sku: string, quantity: number) => Promise<void>;
  clearCart: () => Promise<void>;
  refresh: () => Promise<void>;
}

const CartContext = createContext<CartState | null>(null);

const GUEST_CART_KEY = 'jpetstore_guest_cart';

export function CartProvider({ children }: { children: ReactNode }) {
  const { userId, isAuthenticated } = useAuth();
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(false);

  // Helper to load guest cart from local storage
  const getGuestCart = useCallback((): Cart => {
    const stored = localStorage.getItem(GUEST_CART_KEY);
    if (stored) {
      try { return JSON.parse(stored) as Cart; } catch { /* ignore */ }
    }
    return { userId: userId || 'guest', items: [], itemCount: 0, total: 0 };
  }, [userId]);

  const saveGuestCart = (newCart: Cart) => {
    newCart.itemCount = newCart.items.reduce((sum, item) => sum + item.quantity, 0);
    newCart.total = newCart.items.reduce((sum, item) => sum + Number(item.subtotal), 0);
    localStorage.setItem(GUEST_CART_KEY, JSON.stringify(newCart));
    setCart({ ...newCart });
  };

  const refresh = useCallback(async () => {
    if (!isAuthenticated || !userId) {
      setCart(getGuestCart());
      return;
    }

    setLoading(true);
    try {
      // If user just logged in and has a guest cart, merge it first
      const guestCart = getGuestCart();
      if (guestCart.items.length > 0) {
        for (const item of guestCart.items) {
          await cartApi.addItem(userId, {
            itemSku: item.itemSku,
            productName: item.productName,
            quantity: item.quantity,
            unitPrice: item.unitPrice
          });
        }
        localStorage.removeItem(GUEST_CART_KEY);
      }

      const data = await cartApi.getCart(userId);
      setCart(data);
    } catch {
      // ignore on initial load
    } finally {
      setLoading(false);
    }
  }, [userId, isAuthenticated, getGuestCart]);

  useEffect(() => {
    // refresh is fully async — setState calls happen asynchronously, not synchronously in the effect body
    // eslint-disable-next-line react-hooks/set-state-in-effect
    void refresh();
  }, [refresh]);

  const addItem = async (item: AddToCartRequest) => {
    if (!isAuthenticated || !userId) {
      const gCart = getGuestCart();
      const existing = gCart.items.find(i => i.itemSku === item.itemSku);
      if (existing) {
        existing.quantity += item.quantity;
        existing.subtotal = existing.quantity * existing.unitPrice;
      } else {
        gCart.items.push({
          itemSku: item.itemSku,
          productName: item.productName,
          quantity: item.quantity,
          unitPrice: item.unitPrice,
          subtotal: item.quantity * item.unitPrice
        });
      }
      saveGuestCart(gCart);
      return;
    }

    const data = await cartApi.addItem(userId, item);
    setCart(data);
  };

  const removeItem = async (sku: string) => {
    if (!isAuthenticated || !userId) {
      const gCart = getGuestCart();
      gCart.items = gCart.items.filter(i => i.itemSku !== sku);
      saveGuestCart(gCart);
      return;
    }

    const data = await cartApi.removeItem(userId, sku);
    setCart(data);
  };

  const updateQuantity = async (sku: string, quantity: number) => {
    if (!isAuthenticated || !userId) {
      const gCart = getGuestCart();
      const existing = gCart.items.find(i => i.itemSku === sku);
      if (existing) {
        if (quantity <= 0) {
          gCart.items = gCart.items.filter(i => i.itemSku !== sku);
        } else {
          existing.quantity = quantity;
          existing.subtotal = quantity * existing.unitPrice;
        }
      }
      saveGuestCart(gCart);
      return;
    }

    const data = await cartApi.updateQuantity(userId, sku, quantity);
    setCart(data);
  };

  const clearCart = async () => {
    if (!isAuthenticated || !userId) {
      localStorage.removeItem(GUEST_CART_KEY);
      setCart(null);
      return;
    }

    await cartApi.clearCart(userId);
    setCart(null);
  };

  return (
    <CartContext.Provider value={{
      cart,
      itemCount: cart?.itemCount ?? 0,
      loading,
      addItem,
      removeItem,
      updateQuantity,
      clearCart,
      refresh,
    }}>
      {children}
    </CartContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useCartContext(): CartState {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCartContext must be used within CartProvider');
  return ctx;
}

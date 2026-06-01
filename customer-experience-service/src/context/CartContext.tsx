import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
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

export function CartProvider({ children }: { children: ReactNode }) {
  const { userId } = useAuth();
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(false);

  const refresh = async () => {
    if (!userId) return;
    setLoading(true);
    try {
      const data = await cartApi.getCart(userId);
      setCart(data);
    } catch { /* ignore on initial load */ }
    finally { setLoading(false); }
  };

  useEffect(() => { refresh(); }, [userId]);

  const addItem = async (item: AddToCartRequest) => {
    if (!userId) return;
    const data = await cartApi.addItem(userId, item);
    setCart(data);
  };

  const removeItem = async (sku: string) => {
    if (!userId) return;
    const data = await cartApi.removeItem(userId, sku);
    setCart(data);
  };

  const updateQuantity = async (sku: string, quantity: number) => {
    if (!userId) return;
    const data = await cartApi.updateQuantity(userId, sku, quantity);
    setCart(data);
  };

  const clearCart = async () => {
    if (!userId) return;
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

export function useCartContext(): CartState {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCartContext must be used within CartProvider');
  return ctx;
}

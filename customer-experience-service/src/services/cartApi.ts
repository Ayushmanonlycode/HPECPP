import type { Cart, AddToCartRequest } from '../types/api';
import api from './api';

export const cartApi = {
  getCart: async (userId: string): Promise<Cart> => {
    const res = await api.get<Cart>(`/api/cart/${userId}`);
    return res.data;
  },

  addItem: async (userId: string, item: AddToCartRequest): Promise<Cart> => {
    const res = await api.post<Cart>(`/api/cart/${userId}/items`, item);
    return res.data;
  },

  updateQuantity: async (userId: string, sku: string, quantity: number): Promise<Cart> => {
    const res = await api.put<Cart>(`/api/cart/${userId}/items/${sku}`, { quantity });
    return res.data;
  },

  removeItem: async (userId: string, sku: string): Promise<Cart> => {
    const res = await api.delete<Cart>(`/api/cart/${userId}/items/${sku}`);
    return res.data;
  },

  clearCart: async (userId: string): Promise<void> => {
    await api.delete(`/api/cart/${userId}`);
  },
};

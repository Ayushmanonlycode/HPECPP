import type { Cart, AddToCartRequest } from '../types/api';
import { MOCK_CART } from './mock/mockCart';
import api, { USE_MOCKS } from './api';

const delay = (ms = 300) => new Promise((r) => setTimeout(r, ms));

// In-memory mock cart state (resets on page refresh — acceptable for mock mode)
let mockCart = { ...MOCK_CART };

export const cartApi = {
  getCart: async (userId: string): Promise<Cart> => {
    if (USE_MOCKS) { await delay(); return { ...mockCart, userId }; }
    const res = await api.get<Cart>(`/api/cart/${userId}`);
    return res.data;
  },

  addItem: async (userId: string, item: AddToCartRequest): Promise<Cart> => {
    if (USE_MOCKS) {
      await delay();
      const existing = mockCart.items.find((i) => i.itemSku === item.itemSku);
      if (existing) {
        existing.quantity += item.quantity;
        existing.subtotal = existing.quantity * existing.unitPrice;
      } else {
        mockCart.items.push({
          itemSku: item.itemSku,
          productName: item.productName,
          quantity: item.quantity,
          unitPrice: item.unitPrice,
          subtotal: item.quantity * item.unitPrice,
        });
      }
      mockCart.itemCount = mockCart.items.reduce((s, i) => s + i.quantity, 0);
      mockCart.total = mockCart.items.reduce((s, i) => s + i.subtotal, 0);
      return { ...mockCart, userId };
    }
    const res = await api.post<Cart>(`/api/cart/${userId}/items`, item);
    return res.data;
  },

  updateQuantity: async (userId: string, sku: string, quantity: number): Promise<Cart> => {
    if (USE_MOCKS) {
      await delay();
      const item = mockCart.items.find((i) => i.itemSku === sku);
      if (item) {
        if (quantity <= 0) {
          mockCart.items = mockCart.items.filter((i) => i.itemSku !== sku);
        } else {
          item.quantity = quantity;
          item.subtotal = quantity * item.unitPrice;
        }
      }
      mockCart.itemCount = mockCart.items.reduce((s, i) => s + i.quantity, 0);
      mockCart.total = mockCart.items.reduce((s, i) => s + i.subtotal, 0);
      return { ...mockCart, userId };
    }
    const res = await api.put<Cart>(`/api/cart/${userId}/items/${sku}`, { quantity });
    return res.data;
  },

  removeItem: async (userId: string, sku: string): Promise<Cart> => {
    if (USE_MOCKS) {
      await delay();
      mockCart.items = mockCart.items.filter((i) => i.itemSku !== sku);
      mockCart.itemCount = mockCart.items.reduce((s, i) => s + i.quantity, 0);
      mockCart.total = mockCart.items.reduce((s, i) => s + i.subtotal, 0);
      return { ...mockCart, userId };
    }
    const res = await api.delete<Cart>(`/api/cart/${userId}/items/${sku}`);
    return res.data;
  },

  clearCart: async (userId: string): Promise<void> => {
    if (USE_MOCKS) {
      await delay();
      mockCart.items = [];
      mockCart.itemCount = 0;
      mockCart.total = 0;
      return;
    }
    await api.delete(`/api/cart/${userId}`);
  },
};

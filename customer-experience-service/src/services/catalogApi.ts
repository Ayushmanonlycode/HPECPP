import type { Category, Product, Item } from '../types/api';
import api from './api';

export const catalogApi = {
  getCategories: async (): Promise<Category[]> => {
    const res = await api.get<Category[]>('/api/categories');
    return res.data;
  },

  getCategory: async (id: string): Promise<Category> => {
    const res = await api.get<Category>(`/api/categories/${id}`);
    return res.data;
  },

  getProducts: async (categoryId?: string): Promise<Product[]> => {
    const params = categoryId ? { categoryId } : {};
    const res = await api.get<Product[]>('/api/products', { params });
    return res.data;
  },

  getProduct: async (id: string): Promise<Product> => {
    const res = await api.get<Product>(`/api/products/${id}`);
    return res.data;
  },

  searchProducts: async (q: string): Promise<Product[]> => {
    const res = await api.get<Product[]>('/api/products/search', { params: { q } });
    return res.data;
  },

  getItems: async (productId?: string): Promise<Item[]> => {
    const params = productId ? { productId } : {};
    const res = await api.get<Item[]>('/api/items', { params });
    return res.data;
  },

  getItemBySku: async (sku: string): Promise<Item> => {
    const res = await api.get<Item>(`/api/items/sku/${sku}`);
    return res.data;
  },
};

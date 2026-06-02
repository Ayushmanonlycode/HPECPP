import type { Category, Product, Item } from '../types/api';
import { MOCK_CATEGORIES, MOCK_PRODUCTS, MOCK_ITEMS } from './mock/mockCatalog';
import api, { USE_MOCKS } from './api';

const delay = (ms = 300) => new Promise((r) => setTimeout(r, ms));

export const catalogApi = {
  getCategories: async (): Promise<Category[]> => {
    if (USE_MOCKS) { await delay(); return MOCK_CATEGORIES; }
    const res = await api.get<Category[]>('/api/categories');
    return res.data;
  },

  getCategory: async (id: string): Promise<Category> => {
    if (USE_MOCKS) {
      await delay();
      const cat = MOCK_CATEGORIES.find((c) => c.id === id);
      if (!cat) throw { message: 'Category not found', status: 404 };
      return cat;
    }
    const res = await api.get<Category>(`/api/categories/${id}`);
    return res.data;
  },

  getProducts: async (categoryId?: string): Promise<Product[]> => {
    if (USE_MOCKS) {
      await delay();
      return categoryId
        ? MOCK_PRODUCTS.filter((p) => p.categoryId === categoryId)
        : MOCK_PRODUCTS;
    }
    const params = categoryId ? { categoryId } : {};
    const res = await api.get<Product[]>('/api/products', { params });
    return res.data;
  },

  getProduct: async (id: string): Promise<Product> => {
    if (USE_MOCKS) {
      await delay();
      const p = MOCK_PRODUCTS.find((p) => p.id === id);
      if (!p) throw { message: 'Product not found', status: 404 };
      return p;
    }
    const res = await api.get<Product>(`/api/products/${id}`);
    return res.data;
  },

  searchProducts: async (q: string): Promise<Product[]> => {
    if (USE_MOCKS) {
      await delay();
      const lower = q.toLowerCase();
      return MOCK_PRODUCTS.filter(
        (p) => p.name.toLowerCase().includes(lower) || p.description?.toLowerCase().includes(lower)
      );
    }
    const res = await api.get<Product[]>('/api/products/search', { params: { q } });
    return res.data;
  },

  getItems: async (productId?: string): Promise<Item[]> => {
    if (USE_MOCKS) {
      await delay();
      return productId
        ? MOCK_ITEMS.filter((i) => i.productId === productId)
        : MOCK_ITEMS;
    }
    const params = productId ? { productId } : {};
    const res = await api.get<Item[]>('/api/items', { params });
    return res.data;
  },

  getItemBySku: async (sku: string): Promise<Item> => {
    if (USE_MOCKS) {
      await delay();
      const item = MOCK_ITEMS.find((i) => i.sku === sku);
      if (!item) throw { message: 'Item not found', status: 404 };
      return item;
    }
    const res = await api.get<Item>(`/api/items/sku/${sku}`);
    return res.data;
  },
};

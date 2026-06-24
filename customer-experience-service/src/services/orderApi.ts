import type { Order, CreateOrderRequest } from '../types/api';
import api from './api';

export const orderApi = {
  createOrder: async (dto: CreateOrderRequest): Promise<Order> => {
    const res = await api.post<Order>('/api/orders', dto);
    return res.data;
  },

  getOrder: async (id: string): Promise<Order> => {
    const res = await api.get<Order>(`/api/orders/${id}`);
    return res.data;
  },

  getUserOrders: async (userId: string): Promise<Order[]> => {
    const res = await api.get<Order[]>(`/api/orders/user/${userId}`);
    return res.data;
  },

  listAllOrders: async (): Promise<Order[]> => {
    const res = await api.get<Order[]>('/api/orders');
    return res.data;
  },
};

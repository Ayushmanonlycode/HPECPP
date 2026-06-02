import type { Order, CreateOrderRequest } from '../types/api';
import { MOCK_ORDERS } from './mock/mockOrders';
import api, { USE_MOCKS } from './api';

const delay = (ms = 400) => new Promise((r) => setTimeout(r, ms));
let mockOrders = [...MOCK_ORDERS];

export const orderApi = {
  createOrder: async (dto: CreateOrderRequest): Promise<Order> => {
    if (USE_MOCKS) {
      await delay();
      const newOrder: Order = {
        id: crypto.randomUUID(),
        userId: dto.userId,
        status: 'CREATED',
        totalAmount: dto.lineItems.reduce((s, l) => s + l.quantity * l.unitPrice, 0),
        shippingAddress: dto.shippingAddress,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        lineItems: dto.lineItems.map((l) => ({
          ...l,
          lineTotal: l.quantity * l.unitPrice,
        })),
      };
      mockOrders = [newOrder, ...mockOrders];
      return newOrder;
    }
    const res = await api.post<Order>('/api/orders', dto);
    return res.data;
  },

  getOrder: async (id: string): Promise<Order> => {
    if (USE_MOCKS) {
      await delay();
      const order = mockOrders.find((o) => o.id === id);
      if (!order) throw { message: 'Order not found', status: 404 };
      return order;
    }
    const res = await api.get<Order>(`/api/orders/${id}`);
    return res.data;
  },

  getUserOrders: async (userId: string): Promise<Order[]> => {
    if (USE_MOCKS) {
      await delay();
      return mockOrders.filter((o) => o.userId === userId);
    }
    const res = await api.get<Order[]>(`/api/orders/user/${userId}`);
    return res.data;
  },

  listAllOrders: async (): Promise<Order[]> => {
    if (USE_MOCKS) { await delay(); return mockOrders; }
    const res = await api.get<Order[]>('/api/orders');
    return res.data;
  },
};

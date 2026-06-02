import type { Order } from '../../types/api';

export const MOCK_ORDERS: Order[] = [
  {
    id: 'order-mock-001',
    userId: 'mock-user-001',
    status: 'CONFIRMED',
    totalAmount: 990.00,
    shippingAddress: '42 Canine Way, San Francisco, CA 94102',
    createdAt: '2024-05-20T10:30:00Z',
    updatedAt: '2024-05-20T10:31:00Z',
    lineItems: [
      { itemSku: 'FL-DSH-01', productName: 'Siamese Cat',    quantity: 1, unitPrice: 150.00, lineTotal: 150.00 },
      { itemSku: 'K9-GR-01',  productName: 'Golden Retriever', quantity: 1, unitPrice: 800.00, lineTotal: 800.00 },
      { itemSku: 'FI-BT-01',  productName: 'Betta Fish',     quantity: 2, unitPrice: 20.00,  lineTotal: 40.00 },
    ],
  },
  {
    id: 'order-mock-002',
    userId: 'mock-user-001',
    status: 'CREATED',
    totalAmount: 18.50,
    shippingAddress: '42 Canine Way, San Francisco, CA 94102',
    createdAt: '2024-05-25T14:00:00Z',
    updatedAt: '2024-05-25T14:00:00Z',
    lineItems: [
      { itemSku: 'K9-BD-01', productName: 'Bulldog', quantity: 1, unitPrice: 18.50, lineTotal: 18.50 },
    ],
  },
];

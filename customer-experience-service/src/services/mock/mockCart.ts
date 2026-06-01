import type { Cart } from '../../types/api';

export const MOCK_CART: Cart = {
  userId: 'mock-user-001',
  items: [
    { itemSku: 'FL-DSH-01', productName: 'Siamese Cat',    quantity: 1, unitPrice: 150.00, subtotal: 150.00 },
    { itemSku: 'K9-GR-01',  productName: 'Golden Retriever', quantity: 1, unitPrice: 800.00, subtotal: 800.00 },
    { itemSku: 'FI-BT-01',  productName: 'Premium Betta Fish', quantity: 2, unitPrice: 20.00, subtotal: 40.00 },
  ],
  total: 990.00,
  itemCount: 4,
};

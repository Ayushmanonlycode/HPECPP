import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../hooks/useCart';
import { useAuth } from '../context/AuthContext';
import { orderApi } from '../services/orderApi';
import Button from '../components/Button';
import Spinner from '../components/Spinner';

export default function CheckoutPage() {
  const { cart, clearCart } = useCart();
  const { userId } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!cart || cart.items.length === 0) {
    navigate('/cart');
    return null;
  }

  const handlePlaceOrder = async () => {
    if (!userId) return;
    setLoading(true);
    setError(null);
    try {
      const order = await orderApi.createOrder({
        userId,
        shippingAddress: '123 Placeholder St, Mock City, MOCK 12345',
        lineItems: cart.items.map(item => ({
          itemSku: item.itemSku,
          productName: item.productName,
          quantity: item.quantity,
          unitPrice: item.unitPrice,
        })),
      });
      await clearCart();
      navigate(`/orders/${order.id}/confirmation`);
    } catch (err: any) {
      setError(err.message || 'Failed to place order');
      setLoading(false);
    }
  };

  return (
    <div className="container" style={{ paddingTop: 40, paddingBottom: 80, maxWidth: 600 }}>
      <h1 style={{ fontSize: '2rem', marginBottom: 24 }}>Checkout</h1>
      
      <div style={{ background: 'var(--surface-elevated)', border: '1px solid var(--border)', borderRadius: 'var(--radius-lg)', padding: 32 }}>
        <h2 style={{ marginBottom: 16 }}>Order Review</h2>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginBottom: 24 }}>
          {cart.items.map(item => (
            <div key={item.itemSku} style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span>{item.quantity}x {item.productName}</span>
              <span>${Number(item.subtotal).toFixed(2)}</span>
            </div>
          ))}
        </div>
        
        <div style={{ borderTop: '1px solid var(--border)', paddingTop: 16, marginBottom: 32, display: 'flex', justifyContent: 'space-between', fontWeight: 'bold', fontSize: '1.2rem' }}>
          <span>Total</span>
          <span>${(Number(cart.total) * 1.085).toFixed(2)}</span>
        </div>

        {error && <p style={{ color: 'var(--error)', marginBottom: 16 }}>{error}</p>}

        <Button fullWidth size="lg" onClick={handlePlaceOrder} disabled={loading}>
          {loading ? <Spinner size={20} /> : 'Place Order'}
        </Button>
      </div>
    </div>
  );
}

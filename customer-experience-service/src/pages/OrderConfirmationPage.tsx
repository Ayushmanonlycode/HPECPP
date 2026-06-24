import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { orderApi } from '../services/orderApi';
import type { Order } from '../types/api';
import Button from '../components/Button';
import Spinner from '../components/Spinner';

export default function OrderConfirmationPage() {
  const { id } = useParams<{ id: string }>();
  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    const fetchOrder = async () => {
      try {
        const data = await orderApi.getOrder(id);
        setOrder(data);
      } catch (err: unknown) {
        const message = err instanceof Error ? err.message
          : (err as { message?: string })?.message ?? 'Failed to load order details';
        setError(message);
      } finally {
        setLoading(false);
      }
    };
    fetchOrder();
  }, [id]);

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', paddingTop: 100 }}>
        <Spinner size={48} />
      </div>
    );
  }

  if (error || !order) {
    return (
      <div style={{ textAlign: 'center', paddingTop: 100 }}>
        <h1 style={{ fontSize: '1.5rem', marginBottom: 16 }}>Order Not Found</h1>
        <p style={{ color: 'var(--text-secondary)', marginBottom: 24 }}>{error || 'We could not find the details for this order.'}</p>
        <Link to="/">
          <Button>Return to Home</Button>
        </Link>
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 600, margin: '0 auto', paddingTop: 20, paddingBottom: 20 }}>
      <div style={{ textAlign: 'center', marginBottom: 20 }}>
        <svg style={{ margin: '0 auto', marginBottom: 10 }} width="50" height="50" viewBox="0 0 24 24" fill="none" stroke="var(--success)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/>
        </svg>
        <h1 style={{ fontSize: '1.5rem', marginBottom: 8 }}>Order Confirmed!</h1>
        <p style={{ color: 'var(--text-secondary)' }}>Thank you for your purchase. Your order has been successfully placed.</p>
      </div>

      <div style={{ background: 'var(--surface-elevated)', border: '1px solid var(--border)', borderRadius: 'var(--radius-lg)', padding: 20, marginBottom: 20 }}>
        <h2 style={{ fontSize: '1.1rem', marginBottom: 16, borderBottom: '1px solid var(--border)', paddingBottom: 8 }}>Order Summary</h2>

        <div style={{ display: 'grid', gridTemplateColumns: '120px 1fr', gap: '12px 24px', fontSize: '0.9rem' }}>
          <div style={{ color: 'var(--text-secondary)' }}>Order ID</div>
          <div style={{ fontWeight: 600 }}>{order.id}</div>

          <div style={{ color: 'var(--text-secondary)' }}>Customer</div>
          <div>{order.customerName}</div>

          <div style={{ color: 'var(--text-secondary)' }}>Address</div>
          <div style={{ lineHeight: 1.5 }}>{order.shippingAddress}</div>

          <div style={{ color: 'var(--text-secondary)' }}>Status</div>
          <div style={{ color: 'var(--accent)', fontWeight: 600 }}>{order.status}</div>

          <div style={{ color: 'var(--text-secondary)' }}>Total Amount</div>
          <div style={{ fontSize: '1.1rem', fontWeight: 700 }}>${Number(order.totalAmount).toFixed(2)}</div>
        </div>
      </div>

      <div style={{ display: 'flex', justifyContent: 'center' }}>
        <Link to="/">
          <Button size="lg">Continue Shopping</Button>
        </Link>
      </div>
    </div>
  );
}

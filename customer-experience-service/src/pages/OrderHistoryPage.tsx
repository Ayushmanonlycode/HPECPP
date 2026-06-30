import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { orderApi } from '../services/orderApi';
import type { Order } from '../types/api';
import Spinner from '../components/Spinner';
import EmptyState from '../components/EmptyState';

export default function OrderHistoryPage() {
  const { userId } = useAuth();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!userId) return;
    let cancelled = false;

    const fetchOrders = async () => {
      // setLoading is called inside the async function, not synchronously in the effect body
      setLoading(true);
      try {
        const data = await orderApi.getUserOrders(userId);
        if (!cancelled) setOrders(data);
      } catch (err: unknown) {
        if (!cancelled) {
          const message = err instanceof Error ? err.message
            : (err as { message?: string })?.message ?? 'Failed to load orders';
          setError(message);
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    fetchOrders();
    return () => { cancelled = true; };
  }, [userId]);

  if (loading) return <div className="container" style={{ padding: 80, textAlign: 'center' }}><Spinner size={40} /></div>;

  if (error || orders.length === 0) {
    return (
      <div className="container" style={{ padding: 80 }}>
        <EmptyState title="No orders found" description={error || "You haven't placed any orders yet."} />
      </div>
    );
  }

  return (
    <div className="container" style={{ paddingTop: 40, paddingBottom: 80, maxWidth: 800 }}>
      <h1 style={{ fontSize: '2rem', marginBottom: 32 }}>Order History</h1>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
        {orders.map(order => (
          <div key={order.id} style={{ background: 'var(--surface-elevated)', border: '1px solid var(--border)', borderRadius: 'var(--radius-lg)', padding: 24 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16, borderBottom: '1px solid var(--border)', paddingBottom: 16 }}>
              <div>
                <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Order #{order.id.slice(0, 8)}</p>
                <p style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>Placed on {new Date(order.createdAt).toLocaleDateString()}</p>
              </div>
              <div style={{ textAlign: 'right' }}>
                <p style={{ fontWeight: 'bold', fontSize: '1.2rem', color: 'var(--accent)' }}>${Number(order.totalAmount).toFixed(2)}</p>
                <span style={{ display: 'inline-block', background: 'var(--surface-hover)', padding: '4px 8px', borderRadius: 4, fontSize: '0.75rem', marginTop: 4 }}>{order.status}</span>
              </div>
            </div>
            <div>
              {order.lineItems.map(item => (
                <div key={item.itemSku} style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.9rem', marginBottom: 8 }}>
                  <span>{item.quantity}x {item.productName}</span>
                  <span>${Number(item.lineTotal).toFixed(2)}</span>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

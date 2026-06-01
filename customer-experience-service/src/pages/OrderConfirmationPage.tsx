import { useParams, Link } from 'react-router-dom';
import EmptyState from '../components/EmptyState';
import Button from '../components/Button';

export default function OrderConfirmationPage() {
  const { id } = useParams<{ id: string }>();

  return (
    <div className="container" style={{ paddingTop: 80, paddingBottom: 80, maxWidth: 600 }}>
      <EmptyState
        icon={
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="var(--success)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/>
          </svg>
        }
        title="Order Confirmed!"
        description={`Thank you for your purchase. Your order #${id?.split('-')[0].toUpperCase()} has been successfully placed.`}
        action={
          <Link to="/">
            <Button>Continue Shopping</Button>
          </Link>
        }
      />
    </div>
  );
}

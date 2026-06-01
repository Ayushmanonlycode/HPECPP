import { useNavigate } from 'react-router-dom';
import { useCart } from '../hooks/useCart';
import CartItem from '../components/CartItem';
import EmptyState from '../components/EmptyState';
import Button from '../components/Button';
import Spinner from '../components/Spinner';
import './CartPage.css';

export default function CartPage() {
  const { cart, loading, removeItem, updateQuantity } = useCart();
  const navigate = useNavigate();

  if (loading && !cart) {
    return (
      <div className="cart-page cart-page__loading">
        <Spinner size={48} />
      </div>
    );
  }

  if (!cart || cart.items.length === 0) {
    return (
      <div className="cart-page cart-page__empty">
        <EmptyState
          title="Your cart is empty"
          description="Looks like you haven't added anything to your cart yet."
          action={<Button onClick={() => navigate('/products')}>Browse Products</Button>}
        />
      </div>
    );
  }

  return (
    <div className="cart-page">
      <div className="cart-page__header">
        <h1 className="cart-page__title" style={{ margin: 0, marginBottom: 8 }}>Your Cart</h1>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <p className="cart-page__subtitle" style={{ margin: 0 }}>{cart.itemCount} {cart.itemCount === 1 ? 'item' : 'items'} in your cart.</p>
          <Button variant="primary" size="sm" onClick={() => navigate(-1)}>
            &larr; Go Back
          </Button>
        </div>
      </div>

      <div className="cart-page__layout">
        <div className="cart-page__items">
          {cart.items.map(item => (
            <CartItem
              key={item.itemSku}
              item={item}
              onRemove={removeItem}
              onUpdateQuantity={updateQuantity}
            />
          ))}
        </div>

        <div className="cart-page__summary-wrapper">
          <div className="cart-page__summary">
            <h2 className="cart-page__summary-title">Order Summary</h2>
            
            <div className="cart-page__summary-row">
              <span>Subtotal</span>
              <span>${Number(cart.total).toFixed(2)}</span>
            </div>
            
            <div className="cart-page__summary-row">
              <span>Shipping</span>
              <span className="cart-page__summary-value--muted">Calculated at checkout</span>
            </div>

            <div className="cart-page__summary-row">
              <span>Tax</span>
              <span>${(Number(cart.total) * 0.085).toFixed(2)}</span>
            </div>

            <div className="cart-page__summary-divider" />

            <div className="cart-page__summary-total">
              <span>Total</span>
              <span className="cart-page__summary-total-value">${(Number(cart.total) * 1.085).toFixed(2)}</span>
            </div>

            <Button 
              fullWidth 
              size="lg" 
              onClick={() => navigate('/checkout')}
              className="cart-page__checkout-btn"
            >
              Proceed to Checkout →
            </Button>

            <p className="cart-page__secure-msg">
              <LockIcon /> Secure Encrypted Checkout
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

function LockIcon() {
  return (
    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/>
    </svg>
  );
}

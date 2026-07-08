import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../hooks/useCart';
import { useAuth } from '../context/AuthContext';
import { orderApi } from '../services/orderApi';
import { authApi } from '../services/authApi';
import Button from '../components/Button';
import Spinner from '../components/Spinner';
import './CheckoutPage.css';

export default function CheckoutPage() {
  const { cart, loading: cartLoading, clearCart } = useCart();
  const { userId, user, login } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  // Use state (not ref) so the redirect guard reacts to changes correctly
  const [orderPlaced, setOrderPlaced] = useState(false);

  const [formData, setFormData] = useState({
    fullName: user ? `${user.firstName ?? ''} ${user.lastName ?? ''}`.trim() : '',
    phone: user?.phone ?? '',
    email: user?.email ?? '',
    address1: user?.address ?? '',
    address2: '',
    city: user?.city ?? '',
    state: user?.state ?? '',
    country: user?.country ?? '',
    zip: user?.zip ?? '',
  });

  // Redirect to cart if it's genuinely empty (not just "hasn't loaded yet")
  // and no order has been placed. Navigation is a side effect, so it must
  // run after render commits — calling navigate() during render mutates
  // the router while CheckoutPage itself is still rendering, which React
  // (correctly) rejects.
  const cartIsEmpty = !orderPlaced && !loading && !cartLoading && cart !== null && cart.items.length === 0;

  useEffect(() => {
    if (cartIsEmpty) {
      navigate('/cart');
    }
  }, [cartIsEmpty, navigate]);

  if (cartIsEmpty) {
    return null;
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handlePlaceOrder = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!userId) return;
    setLoading(true);
    setError(null);
    try {
      const fullAddress = `${formData.address1}${formData.address2 ? ', ' + formData.address2 : ''}, ${formData.city}, ${formData.state} ${formData.zip}, ${formData.country}`;

      const order = await orderApi.createOrder({
        userId,
        customerName: formData.fullName,
        shippingAddress: fullAddress,
        lineItems: cart!.items.map(item => ({
          itemSku: item.itemSku,
          productName: item.productName,
          quantity: item.quantity,
          unitPrice: item.unitPrice,
        })),
      });

      // Save shipping address if user has none saved
      if (user && !user.address) {
        try {
          const [firstName, ...rest] = formData.fullName.split(' ');
          const updatedUser = await authApi.updateProfile(userId, {
            firstName: firstName || undefined,
            lastName: rest.join(' ') || undefined,
            phone: formData.phone || undefined,
            address: formData.address1 || undefined,
            city: formData.city || undefined,
            state: formData.state || undefined,
            zip: formData.zip || undefined,
            country: formData.country || undefined,
          });
          login(updatedUser);
        } catch (profileErr) {
          // Non-fatal: silently log — the order still succeeded
          console.error('Failed to update user profile with new address', profileErr);
        }
      }

      setOrderPlaced(true); // block redirect guard before clearing cart
      await clearCart();
      navigate(`/orders/${order.id}/confirmation`);
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message
        : (err as { message?: string })?.message ?? 'Failed to place order';
      setError(message);
      setLoading(false);
    }
  };

  const subtotal = cart?.total ?? 0;
  const shipping = 15.00;
  const tax = subtotal * 0.085;
  const total = subtotal + shipping + tax;

  return (
    <div className="checkout-page">
      <div className="checkout-page__layout">
        <div className="checkout-page__form-container">
          <div className="checkout-page__form-header">
            <h1 className="checkout-page__form-title">Shipping Information</h1>
            <Button variant="primary" size="sm" onClick={() => navigate(-1)}>
              &larr; Go Back
            </Button>
          </div>

          <form id="checkout-form" className="checkout-form" onSubmit={handlePlaceOrder}>
            <div className="checkout-form__group">
              <label>Full Name</label>
              <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} required />
            </div>

            <div className="checkout-form__row">
              <div className="checkout-form__group">
                <label>Phone Number</label>
                <input type="tel" name="phone" value={formData.phone} onChange={handleChange} required />
              </div>
              <div className="checkout-form__group">
                <label>Email</label>
                <input type="email" name="email" value={formData.email} onChange={handleChange} required />
              </div>
            </div>

            <div className="checkout-form__group">
              <label>Address Line 1</label>
              <input type="text" name="address1" value={formData.address1} onChange={handleChange} required />
            </div>

            <div className="checkout-form__group">
              <label>Address Line 2</label>
              <input type="text" name="address2" value={formData.address2} onChange={handleChange} />
            </div>

            <div className="checkout-form__row">
              <div className="checkout-form__group">
                <label>City</label>
                <input type="text" name="city" value={formData.city} onChange={handleChange} required />
              </div>
              <div className="checkout-form__group">
                <label>State / Province</label>
                <input type="text" name="state" value={formData.state} onChange={handleChange} required />
              </div>
            </div>

            <div className="checkout-form__row">
              <div className="checkout-form__group">
                <label>Country</label>
                <input type="text" name="country" value={formData.country} onChange={handleChange} required />
              </div>
              <div className="checkout-form__group">
                <label>Postal Code</label>
                <input type="text" name="zip" value={formData.zip} onChange={handleChange} required />
              </div>
            </div>
          </form>
        </div>

        <div className="checkout-page__review-wrapper">
          <div className="checkout-page__review">
            <h2 className="checkout-page__review-title">Order Review</h2>

            <div className="checkout-page__review-items">
              {cart?.items.map(item => (
                <div key={item.itemSku} className="checkout-page__review-item">
                  <span className="checkout-page__review-item-name">{item.quantity}x {item.productName}</span>
                  <span>${Number(item.subtotal).toFixed(2)}</span>
                </div>
              ))}
            </div>

            <div className="checkout-page__review-divider" />

            <div className="checkout-page__review-row">
              <span>Subtotal</span>
              <span>${Number(subtotal).toFixed(2)}</span>
            </div>
            <div className="checkout-page__review-row">
              <span>Shipping</span>
              <span>${shipping.toFixed(2)}</span>
            </div>
            <div className="checkout-page__review-row">
              <span>Estimated Tax</span>
              <span>${tax.toFixed(2)}</span>
            </div>

            <div className="checkout-page__review-divider" />

            <div className="checkout-page__review-total">
              <span>Total</span>
              <span className="checkout-page__review-total-val">${total.toFixed(2)}</span>
            </div>

            {error && (
              <div className="checkout-page__error">
                {error}
              </div>
            )}

            <Button fullWidth size="lg" type="submit" form="checkout-form" disabled={loading}>
              {loading ? <Spinner size={20} /> : 'Place Order'}
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

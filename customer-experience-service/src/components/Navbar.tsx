import { Link, useNavigate } from 'react-router-dom';
import { useCartContext } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

export default function Navbar() {
  const { itemCount } = useCartContext();
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="navbar">
      <div className="navbar__inner">
        <Link to="/" className="navbar__logo">JPetStore</Link>

        <div className="navbar__actions">
          <Link to="/cart" className="navbar__icon-btn" aria-label="Cart">
            <CartIcon />
            {itemCount > 0 && (
              <span className="navbar__badge">{itemCount > 99 ? '99+' : itemCount}</span>
            )}
          </Link>
          {user ? (
            <div className="navbar__user">
              <span className="navbar__username">{user.firstName || user.username}</span>
              <button className="navbar__icon-btn" onClick={handleLogout} aria-label="Logout" title="Logout">
                <UserIcon />
              </button>
            </div>
          ) : (
            <Link to="/auth" className="navbar__icon-btn" aria-label="Sign in" title="Sign in">
              <UserIcon />
            </Link>
          )}
        </div>
      </div>
    </header>
  );
}

function CartIcon() {
  return (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
      <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
    </svg>
  );
}

function UserIcon() {
  return (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
    </svg>
  );
}

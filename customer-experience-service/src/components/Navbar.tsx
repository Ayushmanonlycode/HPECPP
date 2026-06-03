import { Link, useNavigate } from 'react-router-dom';
import { useCartContext } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import SearchBar from './SearchBar';
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

        <nav className="navbar__nav">
          <Link to="/products" className="navbar__nav-link">Shop</Link>
          <Link to="/products?category=dogs" className="navbar__nav-link">Dogs</Link>
          <Link to="/products?category=cats" className="navbar__nav-link">Cats</Link>
          <Link to="/products?category=fish" className="navbar__nav-link">Fish</Link>
          <Link to="/products?category=birds" className="navbar__nav-link">Birds</Link>
        </nav>

        <div className="navbar__actions">
          <SearchBar />
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
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 0 1-8 0"/>
    </svg>
  );
}

function UserIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
    </svg>
  );
}

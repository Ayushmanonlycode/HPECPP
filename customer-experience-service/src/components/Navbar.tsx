import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCartContext } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import SearchBar from './SearchBar';
import './Navbar.css';

export default function Navbar() {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const { itemCount } = useCartContext();
  const { user, login, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const closeMobileMenu = () => setMobileMenuOpen(false);

  return (
    <header className="navbar">
      <div className="navbar__inner">
        <button className="navbar__hamburger" onClick={() => setMobileMenuOpen(!mobileMenuOpen)} aria-label="Menu">
          <MenuIcon />
        </button>
        <Link to="/" className="navbar__logo" onClick={closeMobileMenu}>JPetStore</Link>

        <nav className={`navbar__nav ${mobileMenuOpen ? 'navbar__nav--open' : ''}`}>
          <Link to="/products" className="navbar__nav-link" onClick={closeMobileMenu}>Shop</Link>
          <Link to="/products?category=dogs" className="navbar__nav-link" onClick={closeMobileMenu}>Dogs</Link>
          <Link to="/products?category=cats" className="navbar__nav-link" onClick={closeMobileMenu}>Cats</Link>
          <Link to="/products?category=reptiles" className="navbar__nav-link" onClick={closeMobileMenu}>Reptiles</Link>
          <Link to="/products?category=fish" className="navbar__nav-link" onClick={closeMobileMenu}>Fish</Link>
          <Link to="/products?category=birds" className="navbar__nav-link" onClick={closeMobileMenu}>Birds</Link>
        </nav>

        <div className="navbar__actions">
          <div className="navbar__search-wrap">
            <SearchBar />
          </div>
          <Link to="/cart" className="navbar__icon-btn" aria-label="Cart">
            <CartIcon />
            {itemCount > 0 && (
              <span className="navbar__badge">{itemCount > 99 ? '99+' : itemCount}</span>
            )}
          </Link>
          {user ? (
            <div className="navbar__user" style={{ position: 'relative' }}>
              <span className="navbar__username">{user.firstName || user.username}</span>
              <button 
                className="navbar__icon-btn" 
                onClick={() => setDropdownOpen(!dropdownOpen)} 
                aria-label="User Menu" 
                title="User Menu"
              >
                <UserIcon />
              </button>
              {dropdownOpen && (
                <div className="navbar__dropdown">
                  <button className="navbar__dropdown-item" onClick={handleLogout}>Logout</button>
                </div>
              )}
            </div>
          ) : (
            <button onClick={() => login()} className="navbar__icon-btn" aria-label="Sign in" title="Sign in" style={{ background: 'none', border: 'none', cursor: 'pointer' }}>
              <UserIcon />
            </button>
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

function MenuIcon() {
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <line x1="3" y1="12" x2="21" y2="12"></line>
      <line x1="3" y1="6" x2="21" y2="6"></line>
      <line x1="3" y1="18" x2="21" y2="18"></line>
    </svg>
  );
}

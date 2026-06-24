import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';
import AuthLayout from '../layouts/AuthLayout';
import { useAuth } from '../context/AuthContext';

// Pages
import HomePage from '../pages/HomePage';
import ProductListingPage from '../pages/ProductListingPage';
import ProductDetailPage from '../pages/ProductDetailPage';
import CartPage from '../pages/CartPage';
import CheckoutPage from '../pages/CheckoutPage';
import OrderConfirmationPage from '../pages/OrderConfirmationPage';
import OrderHistoryPage from '../pages/OrderHistoryPage';
import AuthPage from '../pages/AuthPage';
import ProfilePage from '../pages/ProfilePage';
import NotFoundPage from '../pages/NotFoundPage';

import React from 'react';
import { useLocation } from 'react-router-dom';

// Simple wrapper to protect routes (auth required)
function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  
  if (!isAuthenticated) {
    return <Navigate to="/auth" state={{ from: location.pathname }} replace />;
  }
  return children;
}

// Redirect already-logged-in users away from auth page
function PublicOnlyRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  if (isAuthenticated) {
    const rawFrom = location.state?.from;
    const from = (typeof rawFrom === 'string' && rawFrom.startsWith('/') && rawFrom !== '/auth') ? rawFrom : '/';
    return <Navigate to={from} replace />;
  }
  return children;
}

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Main layout (nav + footer) */}
        <Route element={<MainLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/products" element={<ProductListingPage />} />
          <Route path="/products/:id" element={<ProductDetailPage />} />
          <Route path="/cart" element={<CartPage />} />
          
          <Route path="/checkout" element={
            <ProtectedRoute><CheckoutPage /></ProtectedRoute>
          } />
          <Route path="/orders" element={
            <ProtectedRoute><OrderHistoryPage /></ProtectedRoute>
          } />
          <Route path="/orders/:id/confirmation" element={
            <ProtectedRoute><OrderConfirmationPage /></ProtectedRoute>
          } />
          <Route path="/profile" element={
            <ProtectedRoute><ProfilePage /></ProtectedRoute>
          } />

          <Route path="*" element={<NotFoundPage />} />
        </Route>

        {/* Auth layout (no nav/footer) */}
        <Route element={<AuthLayout />}>
          <Route path="/auth" element={<PublicOnlyRoute><AuthPage /></PublicOnlyRoute>} />
          <Route path="/login" element={<Navigate to="/auth" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

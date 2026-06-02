import { Outlet, Link } from 'react-router-dom';
import './AuthLayout.css';

export default function AuthLayout() {
  return (
    <div className="auth-layout">
      <header className="auth-layout__header">
        <Link to="/" className="auth-layout__logo">JPetStore</Link>
      </header>
      <main className="auth-layout__main">
        <div className="auth-layout__card">
          <Outlet />
        </div>
      </main>
    </div>
  );
}

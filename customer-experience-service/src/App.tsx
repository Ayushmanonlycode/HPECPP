import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext';
import { ToastProvider } from './context/ToastContext';
import AppRouter from './routes/AppRouter';

export default function App() {
  return (
    <AuthProvider>
      <ToastProvider>
        <CartProvider>
          <AppRouter />
        </CartProvider>
      </ToastProvider>
    </AuthProvider>
  );
}

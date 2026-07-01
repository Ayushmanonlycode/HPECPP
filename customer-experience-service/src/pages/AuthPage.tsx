import { useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

export default function AuthPage() {
  const { login } = useAuth();

  useEffect(() => {
    login(); // Auto-redirect to Keycloak
  }, [login]);

  return null;
}

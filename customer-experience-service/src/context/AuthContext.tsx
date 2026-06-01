import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import type { UserProfile } from '../types/api';

interface AuthState {
  user: UserProfile | null;
  userId: string | null;
  isAuthenticated: boolean;
  login: (user: UserProfile) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthState | null>(null);

const GUEST_ID = 'guest-' + Math.random().toString(36).slice(2, 10);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<UserProfile | null>(null);

  // Restore session from localStorage on mount
  useEffect(() => {
    const stored = localStorage.getItem('user');
    if (stored) {
      try { setUser(JSON.parse(stored)); } catch { /* ignore */ }
    }
    // Ensure a userId always exists for cart operations
    if (!localStorage.getItem('userId')) {
      localStorage.setItem('userId', GUEST_ID);
    }
  }, []);

  const login = (profile: UserProfile) => {
    setUser(profile);
    localStorage.setItem('user', JSON.stringify(profile));
    localStorage.setItem('userId', profile.id);
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
    localStorage.setItem('userId', GUEST_ID);
  };

  const userId = user?.id ?? localStorage.getItem('userId') ?? GUEST_ID;

  return (
    <AuthContext.Provider value={{ user, userId, isAuthenticated: !!user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthState {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}

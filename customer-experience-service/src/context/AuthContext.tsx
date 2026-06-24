import { createContext, useContext, useState, type ReactNode } from 'react';
import type { UserProfile } from '../types/api';

interface AuthState {
  user: UserProfile | null;
  userId: string | null;
  isAuthenticated: boolean;
  login: (user: UserProfile) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthState | null>(null);

// Generate a stable guest ID (persisted in localStorage)
function getOrCreateGuestId(): string {
  let guestId = localStorage.getItem('guestId');
  if (!guestId) {
    guestId = 'guest-' + Math.random().toString(36).slice(2, 10);
    localStorage.setItem('guestId', guestId);
  }
  return guestId;
}

// Restore user synchronously so ProtectedRoute never sees a false unauthenticated state
function restoreUser(): UserProfile | null {
  try {
    const stored = localStorage.getItem('user');
    if (stored) return JSON.parse(stored) as UserProfile;
  } catch { /* ignore */ }
  return null;
}

export function AuthProvider({ children }: { children: ReactNode }) {
  // Initialize synchronously from localStorage — no useEffect delay
  const [user, setUser] = useState<UserProfile | null>(restoreUser);

  const login = (profile: UserProfile) => {
    setUser(profile);
    localStorage.setItem('user', JSON.stringify(profile));
    localStorage.setItem('userId', profile.id);
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
    localStorage.removeItem('token');
  };

  const userId = user?.id ?? localStorage.getItem('userId') ?? getOrCreateGuestId();

  return (
    <AuthContext.Provider value={{ user, userId, isAuthenticated: !!user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth(): AuthState {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}

import { createContext, useContext, useState, useEffect, useRef, type ReactNode } from 'react';
import type { UserProfile } from '../types/api';
import Keycloak from 'keycloak-js';

interface AuthState {
  user: UserProfile | null;
  userId: string | null;
  isAuthenticated: boolean;
  login: () => void;
  logout: () => void;
  refreshProfile: () => Promise<void>;
}

const AuthContext = createContext<AuthState | null>(null);

export const keycloak = new Keycloak({
  url: 'http://localhost:8080',
  realm: 'jpetstore',
  clientId: 'jpetstore-frontend'
});

export function AuthProvider({ children }: { children: ReactNode }) {
  const [initialized, setInitialized] = useState(false);
  const [user, setUser] = useState<UserProfile | null>(null);
  const [userId, setUserId] = useState<string | null>(null);
  const didInit = useRef(false);

  useEffect(() => {
    if (didInit.current) return;
    didInit.current = true;

    keycloak.init({ onLoad: 'check-sso', checkLoginIframe: false })
      .then(authenticated => {
        if (authenticated && keycloak.tokenParsed) {
          const profile = keycloak.tokenParsed as any;
          setUserId(keycloak.subject ?? null);
          localStorage.setItem('userId', keycloak.subject ?? '');
          localStorage.setItem('token', keycloak.token ?? '');
          
          import('../services/authApi').then(({ authApi }) => {
            if (keycloak.subject) {
              authApi.getProfile(keycloak.subject).then(savedProfile => {
                setUser({
                  ...savedProfile,
                  // Always override names from Keycloak for consistency
                  firstName: profile.given_name || savedProfile.firstName || '',
                  lastName: profile.family_name || savedProfile.lastName || '',
                  email: profile.email || savedProfile.email || '',
                });
                setInitialized(true);
              }).catch(err => {
                console.error("Failed to fetch full profile, falling back to basic claims:", err);
                setUser({
                  id: keycloak.subject ?? '',
                  username: profile.preferred_username || '',
                  email: profile.email || '',
                  firstName: profile.given_name || '',
                  lastName: profile.family_name || '',
                  phone: '',
                  address: '',
                  address2: '',
                  city: '',
                  state: '',
                  zip: '',
                  country: '',
                  status: 'ACTIVE',
                  createdAt: new Date().toISOString(),
                  updatedAt: new Date().toISOString()
                });
                setInitialized(true);
              });
            }
          });
        } else {
          // Generate guest ID if not logged in
          if (!localStorage.getItem('userId')) {
             localStorage.setItem('userId', 'guest-' + Math.random().toString(36).slice(2, 10));
          }
          setInitialized(true);
        }
      })
      .catch(console.error);

      // Auto-refresh token
      keycloak.onTokenExpired = () => {
        keycloak.updateToken(30).then(refreshed => {
          if (refreshed) {
            localStorage.setItem('token', keycloak.token ?? '');
          }
        });
      };
  }, []);

  const login = () => {
    keycloak.login();
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    keycloak.logout();
  };

  const refreshProfile = async () => {
    const id = keycloak.subject;
    if (!id) return;
    try {
      const { authApi } = await import('../services/authApi');
      const savedProfile = await authApi.getProfile(id);
      const profile = keycloak.tokenParsed as any;
      setUser({
        ...savedProfile,
        firstName: profile?.given_name || savedProfile.firstName || '',
        lastName: profile?.family_name || savedProfile.lastName || '',
        email: profile?.email || savedProfile.email || '',
      });
    } catch (e) {
      console.error('Failed to refresh profile', e);
    }
  };

  if (!initialized) {
    return <div style={{ display: 'flex', height: '100vh', justifyContent: 'center', alignItems: 'center' }}>Loading Application...</div>;
  }

  // Use localStorage guest ID if user is not logged in
  const activeUserId = userId ?? localStorage.getItem('userId');

  return (
    <AuthContext.Provider value={{ user, userId: activeUserId, isAuthenticated: !!user, login, logout, refreshProfile }}>
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

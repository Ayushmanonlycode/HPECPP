import type { UserProfile, LoginRequest } from '../types/api';
import api, { USE_MOCKS } from './api';

const delay = (ms = 300) => new Promise((r) => setTimeout(r, ms));

const MOCK_USER: UserProfile = {
  id: 'mock-user-001',
  username: 'jpetstore_user',
  email: 'user@jpetstore.dev',
  firstName: 'Alex',
  lastName: 'Morgan',
  phone: '+1 555 0100',
  address: '42 Canine Way',
  city: 'San Francisco',
  state: 'CA',
  zip: '94102',
  country: 'US',
  status: 'ACTIVE',
  createdAt: '2024-01-01T00:00:00Z',
  updatedAt: '2024-01-01T00:00:00Z',
};

// NOTE: Auth is a placeholder. Keycloak/OIDC integration is a separate sprint item.
export const authApi = {
  login: async (_dto: LoginRequest): Promise<UserProfile> => {
    if (USE_MOCKS) {
      await delay(600);
      return MOCK_USER;
    }
    // When real auth is available, this will POST to /api/users/login
    const res = await api.post<UserProfile>('/api/users/login', _dto);
    return res.data;
  },

  logout: async (): Promise<void> => {
    await delay(200);
    // Placeholder — no real session to invalidate until Keycloak is integrated
  },

  getProfile: async (userId: string): Promise<UserProfile> => {
    if (USE_MOCKS) { await delay(); return MOCK_USER; }
    const res = await api.get<UserProfile>(`/api/users/${userId}`);
    return res.data;
  },
};

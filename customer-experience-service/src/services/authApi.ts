import type { UserProfile, LoginRequest, RegisterRequest } from '../types/api';
import api from './api';

// NOTE: Auth is a placeholder. Keycloak/OIDC integration is a separate sprint item.
export const authApi = {
  login: async (dto: LoginRequest): Promise<UserProfile> => {
    // When real auth is available, this will POST to /api/users/login
    const res = await api.post<{ token: string; tokenType: string; user: UserProfile }>('/api/users/login', dto);
    if (res.data.token) {
      localStorage.setItem('token', res.data.token);
    }
    return res.data.user;
  },

  register: async (dto: RegisterRequest): Promise<UserProfile> => {
    const res = await api.post<UserProfile>('/api/users/register', dto);
    return res.data;
  },

  logout: async (): Promise<void> => {
    // Placeholder — no real session to invalidate until Keycloak is integrated
  },

  getProfile: async (userId: string): Promise<UserProfile> => {
    const res = await api.get<UserProfile>(`/api/users/${userId}`);
    return res.data;
  },

  updateProfile: async (userId: string, dto: import('../types/api').UserProfileUpdateDto): Promise<UserProfile> => {
    const res = await api.put<UserProfile>(`/api/users/${userId}/profile`, dto);
    return res.data;
  },
};

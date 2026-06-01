import axios from 'axios';

const USE_MOCKS = import.meta.env.VITE_USE_MOCKS === 'true';

const api = axios.create({
  baseURL: USE_MOCKS ? '' : (import.meta.env.VITE_API_BASE_URL || ''),
  headers: { 'Content-Type': 'application/json' },
  timeout: 10000,
});

// Attach userId header from localStorage on every request
api.interceptors.request.use((config) => {
  const userId = localStorage.getItem('userId');
  if (userId) {
    config.headers['X-User-Id'] = userId;
  }
  return config;
});

// Normalise errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.message ||
      error.response?.data ||
      error.message ||
      'An unexpected error occurred';
    return Promise.reject({ message, status: error.response?.status ?? 0 });
  }
);

export { USE_MOCKS };
export default api;

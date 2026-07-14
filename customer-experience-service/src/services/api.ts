import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const userId = localStorage.getItem('userId');
  if (userId) {
    config.headers['X-User-Id'] = userId;
  }
  const token = localStorage.getItem('token');
  // Do not attach the Authorization token for public endpoints (login, register)
  // because Spring Security will attempt to validate it, and if it's expired,
  // it will reject the request with 401 Unauthorized before checking permitAll rules.
  const isPublicEndpoint = config.url?.includes('/register') || config.url?.includes('/login');
  
  if (token && !isPublicEndpoint) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

// Normalise errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    let message = 'An unexpected error occurred';
    
    if (typeof error.response?.data?.message === 'string') {
      message = error.response.data.message;
    } else if (typeof error.response?.data?.error === 'string') {
      // Handle Spring Boot validation error format
      if (error.response.data.fieldErrors) {
        const fields = Object.keys(error.response.data.fieldErrors);
        message = error.response.data.fieldErrors[fields[0]] || error.response.data.error;
      } else {
        message = error.response.data.error;
      }
    } else if (typeof error.response?.data === 'string') {
      message = error.response.data;
    } else if (typeof error.message === 'string') {
      message = error.message;
    }

    return Promise.reject({ message, status: error.response?.status ?? 0 });
  }
);

export default api;

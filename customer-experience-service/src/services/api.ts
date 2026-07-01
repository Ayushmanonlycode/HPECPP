import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  headers: { 'Content-Type': 'application/json' },
});

// Attach userId and Authorization header from localStorage on every request
api.interceptors.request.use((config) => {
  const userId = localStorage.getItem('userId');
  if (userId) {
    config.headers['X-User-Id'] = userId;
  }
  const token = localStorage.getItem('token');
  if (token) {
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

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

export { USE_MOCKS };
export default api;

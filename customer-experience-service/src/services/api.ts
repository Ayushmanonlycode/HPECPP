import axios, { type InternalAxiosRequestConfig } from 'axios';

const api = axios.create({
  baseURL: "",
});

// ── Token validation ────────────────────────────────────────────
// The backend's MP-JWT auth mechanism (WildFly/SmallRye) throws a raw
// 500 instead of a clean 401 when it's handed a token it can't verify —
// even on @PermitAll endpoints, since authentication is attempted before
// the authorization check. This happens whenever a token is expired, OR
// whenever it was signed by a Keycloak instance that no longer exists
// (Keycloak has no persistent volume for its signing keys here, so every
// container recreation issues a brand-new keypair — any token from a
// previous run instantly stops verifying, even though it still "looks"
// unexpired). We can only check expiry/shape client-side; we can't check
// the signature without Keycloak's current public key. So: never attach
// a token that looks bad, AND if the backend rejects one anyway, drop it
// and transparently retry once without it.
function decodeJwtPayload(token: string): { exp?: number } | null {
  try {
    const base64Url = token.split('.')[1];
    if (!base64Url) return null;
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const json = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + c.charCodeAt(0).toString(16).padStart(2, '0'))
        .join('')
    );
    return JSON.parse(json);
  } catch {
    return null;
  }
}

function isTokenUsable(token: string): boolean {
  const payload = decodeJwtPayload(token);
  if (!payload) return false; // malformed — can't be trusted
  if (!payload.exp) return true; // no exp claim, assume non-expiring
  // 10s clock-skew buffer so we don't send a token that expires mid-flight
  return Date.now() < payload.exp * 1000 - 10_000;
}

function clearAuth() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  localStorage.removeItem('userId');
  // Let AuthContext know so isAuthenticated/UI state stays in sync
  window.dispatchEvent(new Event('auth:expired'));
}

// Marks a request as "already retried without auth" so we never loop.
interface RetryableConfig extends InternalAxiosRequestConfig {
  _retriedWithoutAuth?: boolean;
}

// Endpoints that are always anonymous. A stale/unverifiable token attached
// to these is actively harmful: WildFly's MP-JWT mechanism validates any
// Bearer token it receives *before* checking @PermitAll, so a bad token
// makes login/register fail with a 500 even though neither endpoint needed
// auth in the first place. Never attach a token to these, regardless of
// what's sitting in localStorage.
const ANONYMOUS_PATHS = ['/api/users/login', '/api/users/register'];

function isAnonymousPath(url?: string): boolean {
  if (!url) return false;
  return ANONYMOUS_PATHS.some((p) => url.includes(p));
}

// Attach userId and Authorization header from localStorage on every request
api.interceptors.request.use((config: RetryableConfig) => {
  const userId = localStorage.getItem('userId');
  if (userId) {
    config.headers['X-User-Id'] = userId;
  }

  if (isAnonymousPath(config.url)) {
    delete config.headers['Authorization'];
    return config;
  }

  const token = localStorage.getItem('token');
  if (token && !config._retriedWithoutAuth) {
    if (isTokenUsable(token)) {
      config.headers['Authorization'] = `Bearer ${token}`;
    } else {
      // Stale/invalid token — drop it rather than send a request the
      // backend is guaranteed to reject.
      clearAuth();
    }
  }

  return config;
});

// A bad/unverifiable token makes WildFly's MP-JWT layer reject the request
// before it ever reaches our resource method. Depending on WildFly's error
// handling config this can come back as a raw HTML container error page,
// OR as a small JSON body — either way it's not an application-level 500
// our own code produced. We treat any 500 on a request that *carried a
// token* as suspect: worst case we retry once without the token and it
// fails again with the real error; best case we self-heal instead of
// leaving the user stuck in a permanent 500 loop.
function looksLikeAuthRejection(error: {
  response?: { status?: number; data?: unknown };
}): boolean {
  if (error.response?.status !== 500) return false;
  const data = error.response.data;
  if (typeof data === 'string') {
    return data.includes('Internal Server Error') && data.includes('<html>');
  }
  // Any other body shape on a 500 for a token-bearing request is still
  // consistent with the auth layer rejecting the token outright.
  return true;
}

// Normalise errors
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const config = error.config as RetryableConfig | undefined;
    const hadToken = !!config?.headers?.Authorization;

    // Backend rejected our credentials outright — clear them so we
    // don't keep resending a token that will never succeed.
    if (error.response?.status === 401 && localStorage.getItem('token')) {
      clearAuth();
    }

    // A token that passed our client-side check but still got rejected
    // with the backend's generic auth-layer 500 — almost certainly a
    // token signed by a Keycloak instance that's since been recreated.
    // Drop it and retry the same request once, unauthenticated, so
    // public browsing recovers automatically instead of breaking site-wide.
    if (hadToken && !config?._retriedWithoutAuth && looksLikeAuthRejection(error)) {
      clearAuth();
      const retryConfig: RetryableConfig = { ...config!, _retriedWithoutAuth: true };
      delete retryConfig.headers?.Authorization;
      try {
        return await api.request(retryConfig);
      } catch {
        // fall through to normal error handling below using the original error
      }
    }

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

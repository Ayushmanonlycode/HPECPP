import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authApi } from '../services/authApi';
import Button from '../components/Button';
import Spinner from '../components/Spinner';

const Field = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
    <label className="auth-label">{label}</label>
    {children}
  </div>
);

export default function AuthPage() {
  const [tab, setTab] = useState<'login' | 'signup'>('login');

  // Login State
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  // Sign Up State
  const [signupUsername, setSignupUsername] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phone, setPhone] = useState('');
  const [email, setEmail] = useState('');
  const [signupPassword, setSignupPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  // Where to go after success — must be a valid internal path
  const rawFrom = location.state?.from;
  const from = (typeof rawFrom === 'string' && rawFrom.startsWith('/') && rawFrom !== '/auth') ? rawFrom : '/';

  const handleLoginSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const profile = await authApi.login({ username, password });
      login(profile);
      navigate(from, { replace: true });
    } catch (err: any) {
      setError(typeof err.message === 'string' ? err.message : 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  const handleSignupSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (signupPassword !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const profile = await authApi.register({
        username: signupUsername,
        firstName,
        lastName,
        phone,
        email,
        password: signupPassword,
      });
      login(profile);
      navigate(from, { replace: true });
    } catch (err: any) {
      setError(typeof err.message === 'string' ? err.message : 'Sign up failed');
    } finally {
      setLoading(false);
    }
  };

  const tabBtn = (active: boolean) => ({
    flex: 1,
    padding: '12px 0',
    background: 'none',
    border: 'none',
    borderBottom: active ? '2px solid var(--accent)' : '2px solid transparent',
    color: active ? 'var(--text-primary)' : 'var(--text-secondary)',
    fontWeight: active ? 600 : 400,
    cursor: 'pointer',
    fontSize: '1rem',
    transition: 'all 0.2s',
  } as React.CSSProperties);

  return (
    <div style={{ width: '100%' }}>
      {/* Tab switcher */}
      <div style={{ display: 'flex', marginBottom: 28, borderBottom: '1px solid var(--border)' }}>
        <button onClick={() => { setTab('login'); setError(null); }} style={tabBtn(tab === 'login')}>
          Login
        </button>
        <button onClick={() => { setTab('signup'); setError(null); }} style={tabBtn(tab === 'signup')}>
          Sign Up
        </button>
      </div>

      {error && (
        <div style={{ padding: '10px 14px', marginBottom: 20, background: 'var(--error-bg)', color: 'var(--error)', borderRadius: 'var(--radius-sm)', fontSize: '0.875rem', border: '1px solid rgba(239,68,68,0.2)' }}>
          {error}
        </div>
      )}

      {tab === 'login' ? (
        <form onSubmit={handleLoginSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 18 }}>
          <Field label="Username">
            <input type="text" value={username} onChange={e => setUsername(e.target.value)} className="auth-input" placeholder="your_username" required />
          </Field>
          <Field label="Password">
            <input type="password" value={password} onChange={e => setPassword(e.target.value)} className="auth-input" placeholder="••••••••" required />
          </Field>
          <Button fullWidth size="lg" type="submit" disabled={loading} style={{ marginTop: 8 }}>
            {loading ? <Spinner size={20} /> : 'Login'}
          </Button>
        </form>
      ) : (
        <form onSubmit={handleSignupSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          <Field label="Username">
            <input type="text" value={signupUsername} onChange={e => setSignupUsername(e.target.value)} className="auth-input" placeholder="choose_a_username" required />
          </Field>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <Field label="First Name">
              <input type="text" value={firstName} onChange={e => setFirstName(e.target.value)} className="auth-input" placeholder="First" required />
            </Field>
            <Field label="Last Name">
              <input type="text" value={lastName} onChange={e => setLastName(e.target.value)} className="auth-input" placeholder="Last" required />
            </Field>
          </div>
          <Field label="Email">
            <input type="email" value={email} onChange={e => setEmail(e.target.value)} className="auth-input" placeholder="you@example.com" required />
          </Field>
          <Field label="Phone Number">
            <input type="tel" value={phone} onChange={e => setPhone(e.target.value)} className="auth-input" placeholder="+91 98765 43210" required />
          </Field>
          <Field label="Password">
            <input type="password" value={signupPassword} onChange={e => setSignupPassword(e.target.value)} className="auth-input" placeholder="••••••••" required />
          </Field>
          <Field label="Confirm Password">
            <input type="password" value={confirmPassword} onChange={e => setConfirmPassword(e.target.value)} className="auth-input" placeholder="••••••••" required />
          </Field>
          <Button fullWidth size="lg" type="submit" disabled={loading} style={{ marginTop: 8 }}>
            {loading ? <Spinner size={20} /> : 'Create Account'}
          </Button>
        </form>
      )}
    </div>
  );
}

import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Button from '../components/Button';

export default function ProfilePage() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  if (!user) {
    navigate('/login');
    return null;
  }

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="container" style={{ paddingTop: 40, paddingBottom: 80, maxWidth: 600 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 32 }}>
        <h1 style={{ fontSize: '2rem' }}>My Profile</h1>
        <Button variant="outline" onClick={handleLogout}>Log Out</Button>
      </div>

      <div style={{ background: 'var(--surface-elevated)', border: '1px solid var(--border)', borderRadius: 'var(--radius-lg)', padding: 32, display: 'flex', flexDirection: 'column', gap: 24 }}>
        <div>
          <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Name</label>
          <p style={{ fontSize: '1.1rem', color: 'var(--text-primary)' }}>{user.firstName} {user.lastName}</p>
        </div>

        <div>
          <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Email</label>
          <p style={{ fontSize: '1.1rem', color: 'var(--text-primary)' }}>{user.email}</p>
        </div>

        <div>
          <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Phone</label>
          <p style={{ fontSize: '1.1rem', color: 'var(--text-primary)' }}>{user.phone || '—'}</p>
        </div>

        <div style={{ borderTop: '1px solid var(--border)', margin: '8px 0' }} />

        <div>
          <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Address</label>
          <p style={{ fontSize: '1.1rem', color: 'var(--text-primary)' }}>
            {user.address}<br />
            {user.city}, {user.state} {user.zip}<br />
            {user.country}
          </p>
        </div>
      </div>
    </div>
  );
}

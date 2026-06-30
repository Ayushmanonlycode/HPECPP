import { Link } from 'react-router-dom';
import EmptyState from '../components/EmptyState';
import Button from '../components/Button';

export default function NotFoundPage() {
  return (
    <div className="container" style={{ paddingTop: 80, paddingBottom: 80, maxWidth: 600 }}>
      <EmptyState
        icon={
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
          </svg>
        }
        title="Page Not Found"
        description="The page you are looking for doesn't exist or has been moved."
        action={
          <Link to="/">
            <Button>Return Home</Button>
          </Link>
        }
      />
    </div>
  );
}

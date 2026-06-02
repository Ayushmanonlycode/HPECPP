import './Spinner.css';

interface Props { size?: number; }

export default function Spinner({ size = 32 }: Props) {
  return (
    <span className="spinner" style={{ width: size, height: size }} role="status" aria-label="Loading">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
        <circle cx="12" cy="12" r="10" opacity="0.15" />
        <path d="M12 2a10 10 0 0 1 10 10" className="spinner__arc" />
      </svg>
    </span>
  );
}

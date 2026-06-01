import { Link } from 'react-router-dom';
import CategoryNav from '../components/CategoryNav';
import ProductCard from '../components/ProductCard';
import SkeletonCard from '../components/SkeletonCard';
import Button from '../components/Button';
import { useProducts } from '../hooks/useProducts';
import heroDogImg from '../assets/hero-dog.png';
import './HomePage.css';

export default function HomePage() {
  const { products, loading } = useProducts();
  const featuredDogs = products.filter(p => p.categoryId === 'cat-dogs').slice(0, 4);

  return (
    <div className="home-page">

      {/* ── Sidebar ─────────────────────────────────────── */}
      <aside className="home-page__sidebar">
        <CategoryNav />
        <div className="home-page__sidebar-footer">
          <Link to="/help" className="home-page__sidebar-link">
            <HelpIcon /> Help
          </Link>
          <Link to="/settings" className="home-page__sidebar-link">
            <SettingsIcon /> Settings
          </Link>
          <Link to="/login" className="home-page__sidebar-btn">
            Sign In
          </Link>
        </div>
      </aside>

      {/* ── Main ────────────────────────────────────────── */}
      <div className="home-page__main">
        <header className="home-page__header">
          <div>
            <h1 className="home-page__title">Featured Dogs</h1>
            <p className="home-page__subtitle">Premium companions for your high-tech lifestyle.</p>
          </div>
          <Button variant="secondary" size="sm">
            <FilterIcon /> Filter
          </Button>
        </header>

        <section className="home-page__grid">
          {loading
            ? Array.from({ length: 4 }).map((_, i) => <SkeletonCard key={i} />)
            : featuredDogs.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))
          }
        </section>

        {/* ── Hero ──────────────────────────────────────── */}
        <section className="home-page__hero">
          <div className="home-page__hero-content">
            <span className="home-page__hero-badge">New Arrival</span>
            <h2 className="home-page__hero-title">The Next Generation of Canines</h2>
            <p className="home-page__hero-desc">
              Discover our curated selection of high-fidelity companions.<br />
              Effortless sophistication meets uncompromised loyalty.
            </p>
          </div>
          <div className="home-page__hero-image-wrap">
            <img src={heroDogImg} alt="Featured dog" className="home-page__hero-image" />
          </div>
        </section>
      </div>
    </div>
  );
}

function HelpIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/><path d="M12 17h.01"/>
    </svg>
  );
}

function SettingsIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z"/>
      <circle cx="12" cy="12" r="3"/>
    </svg>
  );
}

function FilterIcon() {
  return (
    <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/>
    </svg>
  );
}

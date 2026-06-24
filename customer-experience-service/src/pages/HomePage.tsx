import { Link } from 'react-router-dom';
import ProductCard from '../components/ProductCard';
import SkeletonCard from '../components/SkeletonCard';
import { useProducts } from '../hooks/useProducts';
import heroDogImg from '../assets/hero-dog.png';
import './HomePage.css';

export default function HomePage() {
  const { products, items, loading } = useProducts();
  const featuredDogs = products.filter(p => p.categoryName === 'Dogs').slice(0, 4);

  return (
    <div className="home-page">

      {/* ── Hero ──────────────────────────────────────── */}
      <section className="hero">
        {/* Left content */}
        <div className="hero__content">
          <span className="hero__badge">New Collection</span>
          <h1 className="hero__title">
            The Next Generation<br />of Companions
          </h1>
          <p className="hero__desc">
            Discover our curated selection of high-fidelity companions.<br />
            Effortless sophistication meets uncompromised loyalty.
          </p>

          <div className="hero__cta-row">
            <Link to="/products" className="hero__btn">
              Explore All &nbsp;&rarr;
            </Link>
          </div>

        </div>

        {/* Right image */}
        <div className="hero__image-wrap">
          <img src={heroDogImg} alt="Featured companion" className="hero__image" />
        </div>

        {/* Feature strip pinned to bottom */}
        <div className="hero__features">
          {[
            { icon: <TruckIcon />, title: 'Nationwide Delivery', sub: 'Safe & reliable pet transport' },
            { icon: <ShieldIcon />, title: 'Health Guarantee', sub: 'All pets vet-checked' },
            { icon: <CalendarIcon />, title: '14 Day Home Trial', sub: 'Love them or return them' },
            { icon: <HeadsetIcon />, title: '24/7 Pet Support', sub: "We're here to help" },
          ].map(f => (
            <div key={f.title} className="hero__feature">
              <span className="hero__feature-icon">{f.icon}</span>
              <div>
                <p className="hero__feature-title">{f.title}</p>
                <p className="hero__feature-sub">{f.sub}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ── Featured Dogs ────────────────────────────── */}
      <div className="home-page__main">
        <header className="home-page__header">
          <div>
            <h2 className="home-page__title">Featured Dogs</h2>
            <p className="home-page__subtitle">Premium companions for your high-tech lifestyle.</p>
          </div>
          <Link to="/products?category=dogs" className="home-page__view-all">
            View All Dogs &rarr;
          </Link>
        </header>

        <section className="home-page__grid">
          {loading
            ? Array.from({ length: 4 }).map((_, i) => <SkeletonCard key={i} />)
            : featuredDogs.map((product) => (
                <ProductCard key={product.id} product={product} item={items.find(i => i.productId === product.id)} />
              ))
          }
        </section>
      </div>
    </div>
  );
}

// ── Icons ──────────────────────────────────────────────────

function TruckIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <rect x="1" y="3" width="15" height="13"></rect>
      <polygon points="16 8 20 8 23 11 23 16 16 16 16 8"></polygon>
      <circle cx="5.5" cy="18.5" r="2.5"></circle>
      <circle cx="18.5" cy="18.5" r="2.5"></circle>
    </svg>
  );
}

function ShieldIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
    </svg>
  );
}

function CalendarIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
      <line x1="16" y1="2" x2="16" y2="6"></line>
      <line x1="8" y1="2" x2="8" y2="6"></line>
      <line x1="3" y1="10" x2="21" y2="10"></line>
    </svg>
  );
}

function HeadsetIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M3 18v-6a9 9 0 0 1 18 0v6"></path>
      <path d="M21 19a2 2 0 0 1-2 2h-1v-3a2 2 0 0 1 2-2h1zM3 19a2 2 0 0 0 2 2h1v-3a2 2 0 0 0-2-2H3z"></path>
    </svg>
  );
}

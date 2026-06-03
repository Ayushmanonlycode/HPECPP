import { Link } from 'react-router-dom';
import ProductCard from '../components/ProductCard';
import SkeletonCard from '../components/SkeletonCard';
import Button from '../components/Button';
import { useProducts } from '../hooks/useProducts';
import heroDogImg from '../assets/hero-dog.png';
import './HomePage.css';

const CATEGORIES = [
  { name: 'Fish', path: '/products?category=fish' },
  { name: 'Dogs', path: '/products?category=dogs' },
  { name: 'Reptiles', path: '/products?category=reptiles' },
  { name: 'Cats', path: '/products?category=cats' },
  { name: 'Birds', path: '/products?category=birds' },
];

export default function HomePage() {
  const { products, items, loading } = useProducts();
  const featuredDogs = products.filter(p => p.categoryName === 'Dogs').slice(0, 4);

  return (
    <div className="home-page">
      {/* ── Hero ──────────────────────────────────────── */}
      <section className="home-page__hero">
        <div className="home-page__hero-inner">
          <div className="home-page__hero-content">
            <span className="home-page__hero-badge">New Collection</span>
            <h2 className="home-page__hero-title">The Next Generation of Companions</h2>
            <p className="home-page__hero-desc">
              Discover our curated selection of high-fidelity companions.<br />
              Effortless sophistication meets uncompromised loyalty.
            </p>
            <Link to="/products" style={{ display: 'inline-block', marginTop: '24px' }}>
              <Button variant="outline" size="md">Explore All</Button>
            </Link>
          </div>
          <div className="home-page__hero-image-wrap">
            <img src={heroDogImg} alt="Featured dog" className="home-page__hero-image" />
          </div>
        </div>
      </section>

      {/* ── Category Strip ──────────────────────────────── */}
      <section className="home-page__categories">
        <h3 className="home-page__categories-title">Shop by Category</h3>
        <div className="home-page__categories-list">
          {CATEGORIES.map((cat) => (
            <Link key={cat.name} to={cat.path} className="home-page__category-link">
              {cat.name}
            </Link>
          ))}
        </div>
      </section>

      {/* ── Main / Featured ─────────────────────────────── */}
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



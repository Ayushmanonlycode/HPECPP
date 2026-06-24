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



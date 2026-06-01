import { useSearchParams } from 'react-router-dom';
import CategoryNav from '../components/CategoryNav';
import ProductCard from '../components/ProductCard';
import SkeletonCard from '../components/SkeletonCard';
import EmptyState from '../components/EmptyState';

import { catalogApi } from '../services/catalogApi';
import { useState, useEffect } from 'react';
import type { Product } from '../types/api';
import './HomePage.css'; // Reuse home page layout styles

export default function ProductListingPage() {
  const [searchParams] = useSearchParams();
  const categoryStr = searchParams.get('category');
  const queryStr = searchParams.get('q');

  // We need to map category name to categoryId if filtering by category,
  // but for simplicity in this mock scaffold, the useProducts hook takes categoryId.
  // We'll just fetch all and filter in memory if category name is provided,
  // or use the search API if 'q' is provided.
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);

    const fetchProducts = async () => {
      try {
        if (queryStr) {
          const results = await catalogApi.searchProducts(queryStr);
          if (!cancelled) setProducts(results);
        } else {
          const all = await catalogApi.getProducts();
          if (!cancelled) {
            if (categoryStr) {
              setProducts(all.filter(p => p.categoryName.toLowerCase() === categoryStr.toLowerCase()));
            } else {
              setProducts(all);
            }
          }
        }
      } catch (err) {
        console.error(err);
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    fetchProducts();
    return () => { cancelled = true; };
  }, [categoryStr, queryStr]);

  const title = queryStr ? `Search: ${queryStr}` : (categoryStr ? categoryStr.charAt(0).toUpperCase() + categoryStr.slice(1) : 'All Products');

  return (
    <div className="home-page">
      <aside className="home-page__sidebar">
        <CategoryNav />
      </aside>

      <div className="home-page__main" style={{ overflowY: 'auto' }}>
        <header className="home-page__header">
          <div>
            <h1 className="home-page__title">{title}</h1>
            <p className="home-page__subtitle">
              {products.length} {products.length === 1 ? 'result' : 'results'} found.
            </p>
          </div>
        </header>

        {loading ? (
          <section className="home-page__grid">
            {Array.from({ length: 4 }).map((_, i) => <SkeletonCard key={i} />)}
          </section>
        ) : products.length > 0 ? (
          <section className="home-page__grid">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </section>
        ) : (
          <EmptyState
            title="No products found"
            description="Try adjusting your filters or search query."
          />
        )}
      </div>
    </div>
  );
}

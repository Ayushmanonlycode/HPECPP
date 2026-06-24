import { useSearchParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import ProductCard from '../components/ProductCard';
import SkeletonCard from '../components/SkeletonCard';
import EmptyState from '../components/EmptyState';
import { catalogApi } from '../services/catalogApi';
import type { Product, Item } from '../types/api';
import './HomePage.css'; // Reuse home page layout styles

export default function ProductListingPage() {
  const [searchParams] = useSearchParams();
  const categoryStr = searchParams.get('category');
  const queryStr = searchParams.get('q');

  const [products, setProducts] = useState<Product[]>([]);
  const [items, setItems] = useState<Item[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;

    const fetchProducts = async () => {
      // setLoading called inside the async function, not synchronously in the effect body
      setLoading(true);
      try {
        const [results, allItems] = await Promise.all([
          queryStr ? catalogApi.searchProducts(queryStr) : catalogApi.getProducts(),
          catalogApi.getItems()
        ]);

        if (!cancelled) {
          setItems(allItems);
          if (queryStr) {
            setProducts(results);
          } else if (categoryStr) {
            setProducts(results.filter(p => p.categoryName.toLowerCase() === categoryStr.toLowerCase()));
          } else {
            setProducts(results);
          }
        }
      } catch (err) {
        if (!cancelled) console.error('Failed to fetch products:', err);
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    fetchProducts();
    return () => { cancelled = true; };
  }, [categoryStr, queryStr]);

  const title = queryStr
    ? `Search: ${queryStr}`
    : categoryStr
      ? categoryStr.charAt(0).toUpperCase() + categoryStr.slice(1)
      : 'All Products';

  return (
    <div className="home-page">
      <div className="home-page__main">
        <header className="home-page__header" style={{ paddingTop: '48px' }}>
          <div>
            <h1 className="home-page__title">{title}</h1>
            <p className="home-page__subtitle">
              {products.length} {products.length === 1 ? 'result' : 'results'} found.
            </p>
          </div>
        </header>

        {loading ? (
          <section className="home-page__grid">
            {Array.from({ length: 8 }).map((_, i) => <SkeletonCard key={i} />)}
          </section>
        ) : products.length > 0 ? (
          <section className="home-page__grid">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} item={items.find(i => i.productId === product.id)} />
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

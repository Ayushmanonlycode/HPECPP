import { useState, useEffect } from 'react';
import type { Product, Item } from '../types/api';
import { catalogApi } from '../services/catalogApi';

export function useProducts(categoryId?: string) {
  const [products, setProducts] = useState<Product[]>([]);
  const [items, setItems] = useState<Item[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    const fetch = async () => {
      setLoading(true);
      setError(null);
      try {
        const [productsData, itemsData] = await Promise.all([
          catalogApi.getProducts(categoryId),
          catalogApi.getItems()
        ]);
        if (!cancelled) {
          setProducts(productsData);
          setItems(itemsData);
        }
      } catch (err: unknown) {
        if (!cancelled) {
          const msg = err instanceof Error ? err.message : (err as { message?: string })?.message;
          setError(msg ?? 'Failed to load products');
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    void fetch();
    return () => { cancelled = true; };
  }, [categoryId]);

  return { products, items, loading, error };
}

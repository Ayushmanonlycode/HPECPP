import { useState, useEffect } from 'react';
import type { Product, Item } from '../types/api';
import { catalogApi } from '../services/catalogApi';

export function useProduct(productId: string) {
  const [product, setProduct] = useState<Product | null>(null);
  const [items, setItems] = useState<Item[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!productId) return;
    let cancelled = false;

    const fetch = async () => {
      setLoading(true);
      setError(null);
      try {
        const [p, i] = await Promise.all([
          catalogApi.getProduct(productId),
          catalogApi.getItems(productId),
        ]);
        if (!cancelled) {
          setProduct(p);
          setItems(i);
        }
      } catch (err: unknown) {
        if (!cancelled) {
          const msg = err instanceof Error ? err.message : (err as { message?: string })?.message;
          setError(msg ?? 'Failed to load product');
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    void fetch();
    return () => { cancelled = true; };
  }, [productId]);

  return { product, items, loading, error };
}

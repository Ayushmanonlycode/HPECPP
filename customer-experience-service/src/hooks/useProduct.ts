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
    setLoading(true);
    setError(null);
    Promise.all([
      catalogApi.getProduct(productId),
      catalogApi.getItems(productId),
    ])
      .then(([p, i]) => { if (!cancelled) { setProduct(p); setItems(i); } })
      .catch((err) => { if (!cancelled) setError(err.message ?? 'Failed to load product'); })
      .finally(() => { if (!cancelled) setLoading(false); });
    return () => { cancelled = true; };
  }, [productId]);

  return { product, items, loading, error };
}

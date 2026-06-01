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
    setLoading(true);
    setError(null);
    Promise.all([
      catalogApi.getProducts(categoryId),
      catalogApi.getItems()
    ])
      .then(([productsData, itemsData]) => { 
        if (!cancelled) {
          setProducts(productsData);
          setItems(itemsData);
        }
      })
      .catch((err) => { if (!cancelled) setError(err.message ?? 'Failed to load products'); })
      .finally(() => { if (!cancelled) setLoading(false); });
    return () => { cancelled = true; };
  }, [categoryId]);

  return { products, items, loading, error };
}

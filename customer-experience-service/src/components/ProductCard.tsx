import { Link } from 'react-router-dom';
import type { Product, Item } from '../types/api';
import { useCart } from '../hooks/useCart';
import { useToast } from '../context/ToastContext';
import './ProductCard.css';

interface Props {
  product: Product;
  item?: Item;  // Primary SKU/item to display
}

export default function ProductCard({ product, item }: Props) {
  const { addItem } = useCart();
  const { showToast } = useToast();

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (!item) return;
    try {
      await addItem({
        itemSku: item.sku,
        productName: item.description ?? product.name,
        quantity: 1,
        unitPrice: item.listPrice,
      });
      showToast(`${item.description ?? product.name} added to cart!`);
    } catch {
      showToast(`Failed to add ${item.description ?? product.name} to cart`, 'error');
    }
  };

  return (
    <Link to={`/products/${product.id}`} className="product-card">
      <div className="product-card__image-wrap">
        {item?.imageUrl ? (
          <img src={item.imageUrl} alt={product.name} className="product-card__image" />
        ) : (
          <div className="product-card__image-placeholder">
            <PawIcon />
          </div>
        )}
        {item && item.availableQuantity !== undefined ? (
          <span className={`product-card__badge ${item.availableQuantity <= 0 ? 'out-of-stock' : ''}`}>
            <span className="product-card__badge-dot" />
            {item.availableQuantity > 0 ? 'In Stock' : 'Out of Stock'}
          </span>
        ) : (
          <span className="product-card__badge">
            <span className="product-card__badge-dot" />
            In Stock
          </span>
        )}
      </div>

      <div className="product-card__body">
        <p className="product-card__sku">{item?.sku ?? product.id.slice(0, 8)}</p>
        <p className="product-card__name">{item?.description ?? product.name}</p>
        <div className="product-card__footer">
          <span className="product-card__price">
            ${item ? Number(item.listPrice).toFixed(2) : '—'}
          </span>
          {item && (
            <button
              className="product-card__cart-btn"
              onClick={handleAddToCart}
              disabled={item.availableQuantity !== undefined && item.availableQuantity <= 0}
              aria-label={`Add ${product.name} to cart`}
            >
              <CartIcon />
            </button>
          )}
        </div>
      </div>
    </Link>
  );
}

function PawIcon() {
  return (
    <svg width="40" height="40" viewBox="0 0 24 24" fill="currentColor" opacity="0.15">
      <path d="M11 6.5a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0ZM7.5 3a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5Zm9 0a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5Zm-7 7c-2 0-6 1.5-6 5s3.5 5 6 5 6-1.5 6-5-4-5-6-5Z"/>
    </svg>
  );
}

function CartIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
      <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
    </svg>
  );
}

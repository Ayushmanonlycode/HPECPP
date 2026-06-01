import type { CartItem as CartItemType } from '../types/api';
import './CartItem.css';

interface Props {
  item: CartItemType;
  categoryName?: string;
  onRemove: (sku: string) => void;
  onUpdateQuantity: (sku: string, quantity: number) => void;
}

export default function CartItem({ item, categoryName, onRemove, onUpdateQuantity }: Props) {
  return (
    <div className="cart-item">
      <div className="cart-item__image">
        <div className="cart-item__image-placeholder">
          <PawIcon />
        </div>
      </div>

      <div className="cart-item__content">
        <div className="cart-item__info">
          {categoryName && <span className="cart-item__category">{categoryName}</span>}
          <p className="cart-item__name">{item.productName}</p>
          <p className="cart-item__sku">{item.itemSku}</p>
          <div className="cart-item__qty">
            <button
              className="cart-item__qty-btn"
              onClick={() => onUpdateQuantity(item.itemSku, item.quantity - 1)}
              disabled={item.quantity <= 1}
              aria-label="Decrease quantity"
            >−</button>
            <span className="cart-item__qty-val">{item.quantity}</span>
            <button
              className="cart-item__qty-btn"
              onClick={() => onUpdateQuantity(item.itemSku, item.quantity + 1)}
              aria-label="Increase quantity"
            >+</button>
          </div>
        </div>

        <div className="cart-item__right">
          <button
            className="cart-item__remove"
            onClick={() => onRemove(item.itemSku)}
            aria-label={`Remove ${item.productName}`}
          >
            <TrashIcon />
          </button>
          <span className="cart-item__subtotal">${Number(item.subtotal).toFixed(2)}</span>
        </div>
      </div>
    </div>
  );
}

function PawIcon() {
  return (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="currentColor" opacity="0.2">
      <path d="M11 6.5a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0ZM7.5 3a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5Zm9 0a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5Zm-7 7c-2 0-6 1.5-6 5s3.5 5 6 5 6-1.5 6-5-4-5-6-5Z"/>
    </svg>
  );
}

function TrashIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14H6L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/><path d="M9 6V4h6v2"/>
    </svg>
  );
}

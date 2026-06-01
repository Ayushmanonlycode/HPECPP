import { useParams, Link } from 'react-router-dom';
import { useProduct } from '../hooks/useProduct';
import { useCart } from '../hooks/useCart';
import { useToast } from '../context/ToastContext';
import Spinner from '../components/Spinner';
import EmptyState from '../components/EmptyState';
import Button from '../components/Button';
import './ProductDetailPage.css';

export default function ProductDetailPage() {
  const { id } = useParams<{ id: string }>();
  const { product, items, loading, error } = useProduct(id!);
  const { addItem } = useCart();
  const { showToast } = useToast();

  if (loading) {
    return (
      <div className="container product-detail__loading">
        <Spinner size={48} />
      </div>
    );
  }

  if (error || !product) {
    return (
      <div className="container product-detail__error">
        <EmptyState title="Product Not Found" description={error || 'The product you are looking for does not exist.'} />
      </div>
    );
  }

  return (
    <div className="container product-detail">
      <div className="product-detail__breadcrumb">
        <Link to="/">Home</Link> &gt; <Link to={`/products?category=${product.categoryName.toLowerCase()}`}>{product.categoryName}</Link> &gt; <span>{product.name}</span>
      </div>

      <div className="product-detail__header">
        <h1 className="product-detail__title">{product.name}</h1>
        <p className="product-detail__species">{product.species}</p>
        <p className="product-detail__desc">{product.description}</p>
      </div>

      <div className="product-detail__items">
        <h2 className="product-detail__items-title">Available Variants</h2>
        {items.length === 0 ? (
          <p>No variants available currently.</p>
        ) : (
          <div className="product-detail__items-grid">
            {items.map(item => (
              <div key={item.id} className="product-detail__item-card">
                <div className="product-detail__item-info">
                  <p className="product-detail__item-sku">SKU: {item.sku}</p>
                  <p className="product-detail__item-desc">{item.description}</p>
                  <p className="product-detail__item-price">${Number(item.listPrice).toFixed(2)}</p>
                </div>
                <Button 
                  onClick={async () => {
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
                  }}
                >
                  Add to Cart
                </Button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

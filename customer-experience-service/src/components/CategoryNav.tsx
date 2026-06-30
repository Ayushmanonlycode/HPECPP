import { Link, useSearchParams } from 'react-router-dom';
import './CategoryNav.css';

const CATEGORIES = [
  { id: 'cat-fish', name: 'Fish', icon: <FishIcon /> },
  { id: 'cat-dogs', name: 'Dogs', icon: <DogIcon /> },
  { id: 'cat-reptiles', name: 'Reptiles', icon: <ReptileIcon /> },
  { id: 'cat-cats', name: 'Cats', icon: <CatIcon /> },
  { id: 'cat-birds', name: 'Birds', icon: <BirdIcon /> },
];

export default function CategoryNav() {
  const [searchParams] = useSearchParams();
  const activeCategory = searchParams.get('category');

  return (
    <div className="category-nav">
      <h2 className="category-nav__title">Categories</h2>
      <p className="category-nav__subtitle">Browse by species</p>

      <nav className="category-nav__list" aria-label="Sidebar categories">
        {CATEGORIES.map((cat) => {
          const isActive = activeCategory === cat.name.toLowerCase();
          return (
          <Link
            key={cat.id}
            to={`/products?category=${cat.name.toLowerCase()}`}
            className={`category-nav__link ${isActive ? 'category-nav__link--active' : ''}`}
          >
            <span className="category-nav__icon">{cat.icon}</span>
            {cat.name}
          </Link>
          );
        })}
      </nav>
    </div>
  );
}

function FishIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M11 11.5v.01"/><path d="M15 9.5v.01"/><path d="M7 11.5v.01"/><path d="M4 14.5v.01"/><path d="M22 12c-2.66 0-5.32-2-8-4-2.68 2-5.34 2-8 2-1.33 0-2.67-.67-4-2v6c1.33-1.33 2.67-2 4-2 2.66 0 5.32 2 8 2 2.68-2 5.34-2 8-2Z"/>
    </svg>
  );
}

function DogIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor" opacity="0.9">
      <path d="M11 6.5a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0ZM7.5 3a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5Zm9 0a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5Zm-7 7c-2 0-6 1.5-6 5s3.5 5 6 5 6-1.5 6-5-4-5-6-5Z"/>
    </svg>
  );
}

function ReptileIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M21 16c0-1.1-.9-2-2-2a2 2 0 0 0-2 2v2"/><path d="M7 12c0-1.1-.9-2-2-2a2 2 0 0 0-2 2v2"/><path d="M12 21c-2.76 0-5-2.24-5-5V7c0-2.76 2.24-5 5-5s5 2.24 5 5v9c0 2.76-2.24 5-5 5Z"/>
    </svg>
  );
}

function CatIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M12 5c.67 0 1.35.09 2 .26 1.78-2 5.03-2.84 6.42-2.26 1.4.58-.42 7-.42 7 .57 1.07 1 2.24 1 3.44C21 17.9 16.97 21 12 21s-9-3.1-9-7.56c0-1.25.5-2.4 1-3.44 0 0-1.89-6.42-.5-7 1.39-.58 4.72.23 6.5 2.23A9.04 9.04 0 0 1 12 5Z"/><path d="M8 14v.5"/><path d="M16 14v.5"/><path d="M11.25 16.25h1.5L12 17l-.75-.75Z"/>
    </svg>
  );
}

function BirdIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M22 6c-2 0-3.5 1-5 3a9.97 9.97 0 0 0-5-1C6.48 8 2 12.48 2 18h11.5A8.5 8.5 0 0 0 22 6Z"/><path d="M14 12v.01"/><path d="M2 18l5-5"/>
    </svg>
  );
}

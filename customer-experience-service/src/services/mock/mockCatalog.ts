import type { Category, Product, Item } from '../../types/api';

export const MOCK_CATEGORIES: Category[] = [
  { id: 'cat-fish', name: 'Fish', description: 'A wide selection of fish' },
  { id: 'cat-dogs', name: 'Dogs', description: 'Man\'s best friends' },
  { id: 'cat-reptiles', name: 'Reptiles', description: 'Exotic reptile companions' },
  { id: 'cat-cats', name: 'Cats', description: 'Feline companions' },
  { id: 'cat-birds', name: 'Birds', description: 'Avian companions' },
];

export const MOCK_PRODUCTS: Product[] = [
  { id: 'prod-dogs-1', name: 'Bulldog', description: 'Friendly and loyal', species: 'Canis lupus familiaris', categoryId: 'cat-dogs', categoryName: 'Dogs' },
  { id: 'prod-dogs-2', name: 'Poodle', description: 'Intelligent and elegant', species: 'Canis lupus familiaris', categoryId: 'cat-dogs', categoryName: 'Dogs' },
  { id: 'prod-dogs-3', name: 'Dalmatian', description: 'Energetic and spotted', species: 'Canis lupus familiaris', categoryId: 'cat-dogs', categoryName: 'Dogs' },
  { id: 'prod-dogs-4', name: 'Chow Chow', description: 'Dignified and aloof', species: 'Canis lupus familiaris', categoryId: 'cat-dogs', categoryName: 'Dogs' },
  { id: 'prod-cats-1', name: 'Siamese Cat', description: 'Vocal and social', species: 'Felis catus', categoryId: 'cat-cats', categoryName: 'Cats' },
  { id: 'prod-cats-2', name: 'Golden Retriever', description: 'Gentle and friendly', species: 'Felis catus', categoryId: 'cat-dogs', categoryName: 'Dogs' },
  { id: 'prod-fish-1', name: 'Betta Fish', description: 'Vibrant and territorial', species: 'Betta splendens', categoryId: 'cat-fish', categoryName: 'Fish' },
  { id: 'prod-fish-2', name: 'Goldfish', description: 'Classic freshwater fish', species: 'Carassius auratus', categoryId: 'cat-fish', categoryName: 'Fish' },
  { id: 'prod-reptiles-1', name: 'Iguana', description: 'Majestic green lizard', species: 'Iguana iguana', categoryId: 'cat-reptiles', categoryName: 'Reptiles' },
  { id: 'prod-birds-1', name: 'Amazon Parrot', description: 'Colourful and vocal', species: 'Amazona', categoryId: 'cat-birds', categoryName: 'Birds' },
];

export const MOCK_ITEMS: Item[] = [
  { id: 'item-1', sku: 'K9-BD-01', listPrice: 18.50, description: 'Bulldog Male Adult', imageUrl: null, productId: 'prod-dogs-1', productName: 'Bulldog' },
  { id: 'item-2', sku: 'K9-PO-02', listPrice: 18.50, description: 'Poodle Female Puppy', imageUrl: null, productId: 'prod-dogs-2', productName: 'Poodle' },
  { id: 'item-3', sku: 'K9-DL-01', listPrice: 18.50, description: 'Dalmatian Male Adult', imageUrl: null, productId: 'prod-dogs-3', productName: 'Dalmatian' },
  { id: 'item-4', sku: 'K9-CW-01', listPrice: 18.50, description: 'Chow Chow Male Puppy', imageUrl: null, productId: 'prod-dogs-4', productName: 'Chow Chow' },
  { id: 'item-5', sku: 'FL-DSH-01', listPrice: 150.00, description: 'Adult Female, Pedigree', imageUrl: null, productId: 'prod-cats-1', productName: 'Siamese Cat' },
  { id: 'item-6', sku: 'K9-GR-01', listPrice: 800.00, description: 'Puppy, Male', imageUrl: null, productId: 'prod-cats-2', productName: 'Golden Retriever' },
  { id: 'item-7', sku: 'FI-BT-01', listPrice: 20.00, description: 'Male, Halfmoon', imageUrl: null, productId: 'prod-fish-1', productName: 'Betta Fish' },
  { id: 'item-8', sku: 'FI-GF-01', listPrice: 5.50, description: 'Small, Comet', imageUrl: null, productId: 'prod-fish-2', productName: 'Goldfish' },
  { id: 'item-9', sku: 'RP-IG-01', listPrice: 180.00, description: 'Adult, Green', imageUrl: null, productId: 'prod-reptiles-1', productName: 'Iguana' },
  { id: 'item-10', sku: 'AV-AP-01', listPrice: 350.00, description: 'Blue-Front Amazon', imageUrl: null, productId: 'prod-birds-1', productName: 'Amazon Parrot' },
];

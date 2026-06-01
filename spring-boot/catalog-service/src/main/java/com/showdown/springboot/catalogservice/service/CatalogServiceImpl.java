package com.showdown.springboot.catalogservice.service;

import com.showdown.springboot.catalogservice.dto.CategoryDto;
import com.showdown.springboot.catalogservice.dto.ItemDto;
import com.showdown.springboot.catalogservice.dto.ProductDto;
import com.showdown.springboot.catalogservice.entity.Category;
import com.showdown.springboot.catalogservice.entity.Item;
import com.showdown.springboot.catalogservice.entity.Product;
import com.showdown.springboot.catalogservice.exception.ResourceNotFoundException;
import com.showdown.springboot.catalogservice.repository.CategoryRepository;
import com.showdown.springboot.catalogservice.repository.ItemRepository;
import com.showdown.springboot.catalogservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    public CatalogServiceImpl(CategoryRepository categoryRepository,
                              ProductRepository productRepository,
                              ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
    }

    // ── Categories ───────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> listCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        return toCategoryDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category category = new Category(dto.getName(), dto.getDescription());
        Category saved = categoryRepository.save(category);
        return toCategoryDto(saved);
    }

    @Override
    public CategoryDto updateCategory(String id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        Category saved = categoryRepository.save(category);
        return toCategoryDto(saved);
    }

    @Override
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        categoryRepository.deleteById(id);
    }

    // ── Products ─────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> listProducts() {
        return productRepository.findAll().stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> listProductsByCategory(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", categoryId);
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return toProductDto(product);
    }

    @Override
    public ProductDto createProduct(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", dto.getCategoryId()));
        Product product = new Product(dto.getName(), dto.getDescription(), dto.getSpecies(), category);
        Product saved = productRepository.save(product);
        return toProductDto(saved);
    }

    @Override
    public ProductDto updateProduct(String id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", dto.getCategoryId()));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSpecies(dto.getSpecies());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        return toProductDto(saved);
    }

    @Override
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }

    // ── Items ────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> listItems() {
        return itemRepository.findAll().stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> listItemsByProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", productId);
        }
        return itemRepository.findByProductId(productId).stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItem(String id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        return toItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemBySku(String sku) {
        Item item = itemRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Item", sku));
        return toItemDto(item);
    }

    @Override
    public ItemDto createItem(ItemDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", dto.getProductId()));
        Item item = new Item(dto.getSku(), dto.getListPrice(), dto.getDescription(),
                dto.getImageUrl(), product);
        Item saved = itemRepository.save(item);
        return toItemDto(saved);
    }

    @Override
    public ItemDto updateItem(String id, ItemDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", dto.getProductId()));
        item.setSku(dto.getSku());
        item.setListPrice(dto.getListPrice());
        item.setDescription(dto.getDescription());
        item.setImageUrl(dto.getImageUrl());
        item.setProduct(product);
        Item saved = itemRepository.save(item);
        return toItemDto(saved);
    }

    @Override
    public void deleteItem(String id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item", id);
        }
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItems(String keyword) {
        return itemRepository.findByDescriptionContainingIgnoreCase(keyword).stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    // ── Mapping helpers ──────────────────────────────────────────

    private CategoryDto toCategoryDto(Category entity) {
        return new CategoryDto(entity.getId(), entity.getName(), entity.getDescription());
    }

    private ProductDto toProductDto(Product entity) {
        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getSpecies(),
                entity.getCategory().getId(),
                entity.getCategory().getName()
        );
    }

    private ItemDto toItemDto(Item entity) {
        return new ItemDto(
                entity.getId(),
                entity.getSku(),
                entity.getListPrice(),
                entity.getDescription(),
                entity.getImageUrl(),
                entity.getProduct().getId(),
                entity.getProduct().getName()
        );
    }
}


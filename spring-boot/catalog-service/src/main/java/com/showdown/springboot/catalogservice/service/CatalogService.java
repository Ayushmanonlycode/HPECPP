package com.showdown.springboot.catalogservice.service;

import com.showdown.springboot.catalogservice.dto.CategoryDto;
import com.showdown.springboot.catalogservice.dto.ItemDto;
import com.showdown.springboot.catalogservice.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public interface CatalogService {

    // ── Categories ───────────────────────────────────────────────
    List<CategoryDto> listCategories();

    CategoryDto getCategory(UUID id);

    CategoryDto createCategory(CategoryDto dto);

    CategoryDto updateCategory(UUID id, CategoryDto dto);

    void deleteCategory(UUID id);

    // ── Products ─────────────────────────────────────────────────
    List<ProductDto> listProducts();

    List<ProductDto> listProductsByCategory(UUID categoryId);

    ProductDto getProduct(UUID id);

    ProductDto createProduct(ProductDto dto);

    ProductDto updateProduct(UUID id, ProductDto dto);

    void deleteProduct(UUID id);

    List<ProductDto> searchProducts(String keyword);

    // ── Items ────────────────────────────────────────────────────
    List<ItemDto> listItems();

    List<ItemDto> listItemsByProduct(UUID productId);

    ItemDto getItem(UUID id);

    ItemDto getItemBySku(String sku);

    ItemDto createItem(ItemDto dto);

    ItemDto updateItem(UUID id, ItemDto dto);

    void deleteItem(UUID id);

    List<ItemDto> searchItems(String keyword);
}

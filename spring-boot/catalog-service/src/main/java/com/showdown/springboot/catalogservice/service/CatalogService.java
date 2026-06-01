package com.showdown.springboot.catalogservice.service;

import com.showdown.springboot.catalogservice.dto.CategoryDto;
import com.showdown.springboot.catalogservice.dto.ItemDto;
import com.showdown.springboot.catalogservice.dto.ProductDto;

import java.util.List;


public interface CatalogService {

    // ── Categories ───────────────────────────────────────────────
    List<CategoryDto> listCategories();

    CategoryDto getCategory(String id);

    CategoryDto createCategory(CategoryDto dto);

    CategoryDto updateCategory(String id, CategoryDto dto);

    void deleteCategory(String id);

    // ── Products ─────────────────────────────────────────────────
    List<ProductDto> listProducts();

    List<ProductDto> listProductsByCategory(String categoryId);

    ProductDto getProduct(String id);

    ProductDto createProduct(ProductDto dto);

    ProductDto updateProduct(String id, ProductDto dto);

    void deleteProduct(String id);

    List<ProductDto> searchProducts(String keyword);

    // ── Items ────────────────────────────────────────────────────
    List<ItemDto> listItems();

    List<ItemDto> listItemsByProduct(String productId);

    ItemDto getItem(String id);

    ItemDto getItemBySku(String sku);

    ItemDto createItem(ItemDto dto);

    ItemDto updateItem(String id, ItemDto dto);

    void deleteItem(String id);

    List<ItemDto> searchItems(String keyword);
}


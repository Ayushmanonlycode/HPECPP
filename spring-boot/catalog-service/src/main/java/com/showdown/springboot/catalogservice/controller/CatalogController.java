package com.showdown.springboot.catalogservice.controller;

import com.showdown.springboot.catalogservice.dto.CategoryDto;
import com.showdown.springboot.catalogservice.dto.ItemDto;
import com.showdown.springboot.catalogservice.dto.ProductDto;
import com.showdown.springboot.catalogservice.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // ── Categories ───────────────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> listCategories() {
        return ResponseEntity.ok(catalogService.listCategories());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String id) {
        return ResponseEntity.ok(catalogService.getCategory(id));
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto dto) {
        CategoryDto created = catalogService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String id,
                                                      @Valid @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(catalogService.updateCategory(id, dto));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        catalogService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ── Products ─────────────────────────────────────────────────

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> listProducts(
            @RequestParam(required = false) String categoryId) {
        if (categoryId != null) {
            return ResponseEntity.ok(catalogService.listProductsByCategory(categoryId));
        }
        return ResponseEntity.ok(catalogService.listProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(catalogService.getProduct(id));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto) {
        ProductDto created = catalogService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String id,
                                                    @Valid @RequestBody ProductDto dto) {
        return ResponseEntity.ok(catalogService.updateProduct(id, dto));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        catalogService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String q) {
        return ResponseEntity.ok(catalogService.searchProducts(q));
    }

    // ── Items ────────────────────────────────────────────────────

    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> listItems(
            @RequestParam(required = false) String productId) {
        if (productId != null) {
            return ResponseEntity.ok(catalogService.listItemsByProduct(productId));
        }
        return ResponseEntity.ok(catalogService.listItems());
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable String id) {
        return ResponseEntity.ok(catalogService.getItem(id));
    }

    @GetMapping("/items/sku/{sku}")
    public ResponseEntity<ItemDto> getItemBySku(@PathVariable String sku) {
        return ResponseEntity.ok(catalogService.getItemBySku(sku));
    }

    @PostMapping("/items")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto dto) {
        ItemDto created = catalogService.createItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable String id,
                                              @Valid @RequestBody ItemDto dto) {
        return ResponseEntity.ok(catalogService.updateItem(id, dto));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        catalogService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam String q) {
        return ResponseEntity.ok(catalogService.searchItems(q));
    }
}


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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ItemRepository itemRepository;

    private CatalogServiceImpl catalogService;

    @BeforeEach
    void setUp() {
        catalogService = new CatalogServiceImpl(categoryRepository, productRepository, itemRepository);
    }

    // ── Category Tests ───────────────────────────────────────────

    @Test
    void listCategories_shouldReturnCategoryDtoList() {
        Category category = new Category("Dogs", "All kinds of dogs");
        category.setId(java.util.UUID.randomUUID().toString());
        
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryDto> result = catalogService.listCategories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Dogs");
        assertThat(result.get(0).getDescription()).isEqualTo("All kinds of dogs");
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategory_whenExists_shouldReturnCategoryDto() {
        String id = java.util.UUID.randomUUID().toString();
        Category category = new Category("Cats", "Feline friends");
        category.setId(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryDto result = catalogService.getCategory(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Cats");
    }

    @Test
    void getCategory_whenNotExists_shouldThrowResourceNotFoundException() {
        String id = java.util.UUID.randomUUID().toString();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> catalogService.getCategory(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");
    }

    @Test
    void createCategory_shouldSaveAndReturnCategoryDto() {
        CategoryDto dto = new CategoryDto(null, "Birds", "Feathered pets");
        Category savedCategory = new Category("Birds", "Feathered pets");
        savedCategory.setId(java.util.UUID.randomUUID().toString());

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDto result = catalogService.createCategory(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Birds");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    // ── Product Tests ────────────────────────────────────────────

    @Test
    void listProductsByCategory_whenCategoryExists_shouldReturnProducts() {
        String categoryId = java.util.UUID.randomUUID().toString();
        Category category = new Category("Dogs", "Dogs description");
        category.setId(categoryId);
        Product product = new Product("Golden Retriever", "Friendly dog", "Canis", category);
        product.setId(java.util.UUID.randomUUID().toString());

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(productRepository.findByCategoryId(categoryId)).thenReturn(List.of(product));

        List<ProductDto> result = catalogService.listProductsByCategory(categoryId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Golden Retriever");
    }

    @Test
    void listProductsByCategory_whenCategoryDoesNotExist_shouldThrowException() {
        String categoryId = java.util.UUID.randomUUID().toString();
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThatThrownBy(() -> catalogService.listProductsByCategory(categoryId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createProduct_whenCategoryExists_shouldSaveProduct() {
        String categoryId = java.util.UUID.randomUUID().toString();
        Category category = new Category("Dogs", "Dogs description");
        category.setId(categoryId);
        ProductDto dto = new ProductDto(null, "Pug", "Cute pug", "Canis", categoryId, "Dogs");
        
        Product savedProduct = new Product("Pug", "Cute pug", "Canis", category);
        savedProduct.setId(java.util.UUID.randomUUID().toString());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDto result = catalogService.createProduct(dto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Pug");
        assertThat(result.getCategoryName()).isEqualTo("Dogs");
    }

    // ── Item Tests ───────────────────────────────────────────────

    @Test
    void getItemBySku_whenExists_shouldReturnItemDto() {
        Category category = new Category("Dogs", "Dogs");
        category.setId(java.util.UUID.randomUUID().toString());
        Product product = new Product("Poodle", "Smart dog", "Canis", category);
        product.setId(java.util.UUID.randomUUID().toString());
        Item item = new Item("EST-101", new BigDecimal("99.99"), "Toy Poodle", "poodle.jpg", product);
        item.setId(java.util.UUID.randomUUID().toString());

        when(itemRepository.findBySku("EST-101")).thenReturn(Optional.of(item));

        ItemDto result = catalogService.getItemBySku("EST-101");

        assertThat(result).isNotNull();
        assertThat(result.getSku()).isEqualTo("EST-101");
        assertThat(result.getListPrice()).isEqualTo(new BigDecimal("99.99"));
        assertThat(result.getProductName()).isEqualTo("Poodle");
    }
}



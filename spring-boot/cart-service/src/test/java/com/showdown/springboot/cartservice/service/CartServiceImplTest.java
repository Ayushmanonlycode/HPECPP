package com.showdown.springboot.cartservice.service;

import com.showdown.springboot.cartservice.dto.AddToCartDto;
import com.showdown.springboot.cartservice.dto.CartDto;
import com.showdown.springboot.cartservice.dto.CartItemDto;
import com.showdown.springboot.cartservice.model.Cart;
import com.showdown.springboot.cartservice.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private RedisTemplate<String, Cart> redisTemplate;

    @Mock
    private ValueOperations<String, Cart> valueOperations;

    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        cartService = new CartServiceImpl(redisTemplate);
        ReflectionTestUtils.setField(cartService, "cartTtlHours", 24L);
    }

    @Test
    void getCart_whenCartDoesNotExist_shouldReturnEmptyCart() {
        when(valueOperations.get("cart:user1")).thenReturn(null);

        CartDto result = cartService.getCart("user1");

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("user1");
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotal()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void getCart_whenCartExists_shouldReturnCart() {
        Cart cart = new Cart("user1");
        CartItem item = new CartItem("SKU-1", "Product-1", 2, new BigDecimal("10.00"));
        cart.getItems().put("SKU-1", item);

        when(valueOperations.get("cart:user1")).thenReturn(cart);

        CartDto result = cartService.getCart("user1");

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("user1");
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("20.00"));
    }

    @Test
    void addItem_whenNew_shouldAddItemAndSave() {
        Cart cart = new Cart("user1");
        when(valueOperations.get("cart:user1")).thenReturn(cart);

        AddToCartDto dto = new AddToCartDto();
        dto.setItemSku("SKU-1");
        dto.setProductName("Product-1");
        dto.setQuantity(3);
        dto.setUnitPrice(new BigDecimal("5.00"));

        CartDto result = cartService.addItem("user1", dto);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("15.00"));
        verify(valueOperations, times(1)).set(eq("cart:user1"), eq(cart), eq(24L), eq(TimeUnit.HOURS));
    }

    @Test
    void addItem_whenExists_shouldIncrementQuantityAndSave() {
        Cart cart = new Cart("user1");
        CartItem item = new CartItem("SKU-1", "Product-1", 2, new BigDecimal("10.00"));
        cart.getItems().put("SKU-1", item);

        when(valueOperations.get("cart:user1")).thenReturn(cart);

        AddToCartDto dto = new AddToCartDto();
        dto.setItemSku("SKU-1");
        dto.setProductName("Product-1");
        dto.setQuantity(3);
        dto.setUnitPrice(new BigDecimal("10.00"));

        CartDto result = cartService.addItem("user1", dto);

        assertThat(result.getItems()).hasSize(1);
        CartItemDto resultItem = result.getItems().get(0);
        assertThat(resultItem.getQuantity()).isEqualTo(5); // 2 + 3
        assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    void updateItemQuantity_shouldModifyCountAndSave() {
        Cart cart = new Cart("user1");
        CartItem item = new CartItem("SKU-1", "Product-1", 2, new BigDecimal("10.00"));
        cart.getItems().put("SKU-1", item);

        when(valueOperations.get("cart:user1")).thenReturn(cart);

        CartDto result = cartService.updateItemQuantity("user1", "SKU-1", 5);

        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(5);
        assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    void updateItemQuantity_whenCountIsZero_shouldRemoveItem() {
        Cart cart = new Cart("user1");
        CartItem item = new CartItem("SKU-1", "Product-1", 2, new BigDecimal("10.00"));
        cart.getItems().put("SKU-1", item);

        when(valueOperations.get("cart:user1")).thenReturn(cart);

        CartDto result = cartService.updateItemQuantity("user1", "SKU-1", 0);

        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void removeItem_shouldRemoveSkuAndSave() {
        Cart cart = new Cart("user1");
        CartItem item = new CartItem("SKU-1", "Product-1", 2, new BigDecimal("10.00"));
        cart.getItems().put("SKU-1", item);

        when(valueOperations.get("cart:user1")).thenReturn(cart);

        CartDto result = cartService.removeItem("user1", "SKU-1");

        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void clearCart_shouldDeleteKey() {
        cartService.clearCart("user1");
        verify(redisTemplate, times(1)).delete("cart:user1");
    }
}

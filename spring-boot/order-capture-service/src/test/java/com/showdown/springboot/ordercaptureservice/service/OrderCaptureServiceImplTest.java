package com.showdown.springboot.ordercaptureservice.service;

import com.showdown.springboot.ordercaptureservice.dto.CreateOrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderLineItemDto;
import com.showdown.springboot.ordercaptureservice.entity.Order;
import com.showdown.springboot.ordercaptureservice.entity.OrderStatus;
import com.showdown.springboot.ordercaptureservice.exception.InvalidOrderException;
import com.showdown.springboot.ordercaptureservice.exception.ResourceNotFoundException;
import com.showdown.springboot.ordercaptureservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderCaptureServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderCaptureServiceImpl orderCaptureService;

    @BeforeEach
    void setUp() {
        orderCaptureService = new OrderCaptureServiceImpl(orderRepository);
    }

    @Test
    void createOrder_shouldCalculateTotalAndSaveOrder() {
        UUID userId = UUID.randomUUID();
        CreateOrderDto createDto = new CreateOrderDto();
        createDto.setUserId(userId);
        createDto.setShippingAddress("123 Main St");

        OrderLineItemDto item1 = new OrderLineItemDto();
        item1.setItemSku("EST-1");
        item1.setProductName("Goldfish");
        item1.setQuantity(2);
        item1.setUnitPrice(new BigDecimal("10.00"));

        OrderLineItemDto item2 = new OrderLineItemDto();
        item2.setItemSku("EST-2");
        item2.setProductName("Angelfish");
        item2.setQuantity(3);
        item2.setUnitPrice(new BigDecimal("15.00"));

        createDto.setLineItems(List.of(item1, item2));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(UUID.randomUUID());
            return savedOrder;
        });

        OrderDto result = orderCaptureService.createOrder(createDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED.name());
        assertThat(result.getShippingAddress()).isEqualTo("123 Main St");
        assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("65.00")); // 2*10 + 3*15
        assertThat(result.getLineItems()).hasSize(2);
    }

    @Test
    void confirmOrder_whenStatusIsCreated_shouldConfirmOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(new BigDecimal("50.00"));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto result = orderCaptureService.confirmOrder(orderId);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.CONFIRMED.name());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void confirmOrder_whenStatusIsNotCreated_shouldThrowInvalidOrderException() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderCaptureService.confirmOrder(orderId))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessageContaining("Cannot confirm order in status");
    }

    @Test
    void cancelOrder_whenStatusIsCreated_shouldCancelOrder() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto result = orderCaptureService.cancelOrder(orderId);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.CANCELLED.name());
    }

    @Test
    void cancelOrder_whenAlreadyCancelled_shouldThrowInvalidOrderException() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderCaptureService.cancelOrder(orderId))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessageContaining("Order is already cancelled");
    }

    @Test
    void getOrder_whenNotExists_shouldThrowResourceNotFoundException() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderCaptureService.getOrder(orderId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

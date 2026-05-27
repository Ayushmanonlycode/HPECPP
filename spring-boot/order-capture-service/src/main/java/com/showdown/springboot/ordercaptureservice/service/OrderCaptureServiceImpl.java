package com.showdown.springboot.ordercaptureservice.service;

import com.showdown.springboot.ordercaptureservice.dto.CreateOrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderLineItemDto;
import com.showdown.springboot.ordercaptureservice.entity.Order;
import com.showdown.springboot.ordercaptureservice.entity.OrderLineItem;
import com.showdown.springboot.ordercaptureservice.entity.OrderStatus;
import com.showdown.springboot.ordercaptureservice.exception.InvalidOrderException;
import com.showdown.springboot.ordercaptureservice.exception.ResourceNotFoundException;
import com.showdown.springboot.ordercaptureservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderCaptureServiceImpl implements OrderCaptureService {

    private final OrderRepository orderRepository;

    public OrderCaptureServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto createOrder(CreateOrderDto dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setShippingAddress(dto.getShippingAddress());
        order.setStatus(OrderStatus.CREATED);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderLineItemDto itemDto : dto.getLineItems()) {
            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setItemSku(itemDto.getItemSku());
            lineItem.setProductName(itemDto.getProductName());
            lineItem.setQuantity(itemDto.getQuantity());
            lineItem.setUnitPrice(itemDto.getUnitPrice());
            lineItem.calculateLineTotal();
            order.addLineItem(lineItem);
            total = total.add(lineItem.getLineTotal());
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        return toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUser(UUID userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto confirmOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderException(
                    "Cannot confirm order in status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CONFIRMED);
        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    public OrderDto cancelOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderException("Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> listAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ── Mapping helpers ──────────────────────────────────────────

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setLineItems(order.getLineItems().stream()
                .map(this::toLineItemDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private OrderLineItemDto toLineItemDto(OrderLineItem item) {
        OrderLineItemDto dto = new OrderLineItemDto();
        dto.setItemSku(item.getItemSku());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setLineTotal(item.getLineTotal());
        return dto;
    }
}

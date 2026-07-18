package com.showdown.springboot.ordercaptureservice.service;

import com.showdown.springboot.ordercaptureservice.dto.CreateOrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderLineItemDto;
import com.showdown.springboot.ordercaptureservice.entity.Order;
import com.showdown.springboot.ordercaptureservice.entity.OrderLineItem;
import com.showdown.springboot.ordercaptureservice.entity.OrderStatus;
import com.showdown.springboot.ordercaptureservice.exception.InvalidOrderException;
import com.showdown.springboot.ordercaptureservice.exception.ResourceNotFoundException;
import com.showdown.springboot.ordercaptureservice.client.InventoryClient;
import com.showdown.springboot.ordercaptureservice.client.OrderFulfilmentClient;
import com.showdown.springboot.ordercaptureservice.client.UserClient;
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
    private final InventoryClient inventoryClient;
    private final OrderFulfilmentClient orderFulfilmentClient;
    private final UserClient userClient;

    public OrderCaptureServiceImpl(OrderRepository orderRepository,
                                   InventoryClient inventoryClient,
                                   OrderFulfilmentClient orderFulfilmentClient,
                                   UserClient userClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.orderFulfilmentClient = orderFulfilmentClient;
        this.userClient = userClient;
    }

    @Override
    public OrderDto createOrder(CreateOrderDto dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setCustomerName(dto.getCustomerName());
        order.setShippingAddress(dto.getShippingAddress());
        order.setStatus(OrderStatus.CREATED);

        // User validation is now handled intrinsically by Keycloak JWT verification

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderLineItemDto itemDto : dto.getLineItems()) {
            // Directly decrement stock on order placement — no reservation needed
            boolean decremented = inventoryClient.adjustStock(itemDto.getItemSku(), -itemDto.getQuantity());
            if (!decremented) {
                throw new InvalidOrderException("Insufficient stock or inventory service unavailable for SKU: " + itemDto.getItemSku());
            }

            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setItemSku(itemDto.getItemSku());
            lineItem.setProductName(itemDto.getProductName());
            lineItem.setQuantity(itemDto.getQuantity());
            lineItem.setUnitPrice(itemDto.getUnitPrice());
            lineItem.calculateLineTotal();
            order.addLineItem(lineItem);
            subtotal = subtotal.add(lineItem.getLineTotal());
        }

        BigDecimal shipping = new BigDecimal("15.00");
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.085"));
        BigDecimal total = subtotal.add(shipping).add(tax);

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);

        // Kick off the fulfilment process
        orderFulfilmentClient.createFulfilment(saved.getId());

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        return toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUser(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto confirmOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderException(
                    "Cannot confirm order in status: " + order.getStatus());
        }

        // Stock was already deducted at order creation — just mark as CONFIRMED
        order.setStatus(OrderStatus.CONFIRMED);
        Order saved = orderRepository.save(order);
        return toDto(saved);
    }

    @Override
    public OrderDto cancelOrder(String id) {
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
        dto.setCustomerName(order.getCustomerName());
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

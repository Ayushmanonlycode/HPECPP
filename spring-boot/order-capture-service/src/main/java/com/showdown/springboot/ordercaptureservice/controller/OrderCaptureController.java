package com.showdown.springboot.ordercaptureservice.controller;

import com.showdown.springboot.ordercaptureservice.dto.CreateOrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderDto;
import com.showdown.springboot.ordercaptureservice.service.OrderCaptureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order creation, retrieval, confirmation, and cancellation")
public class OrderCaptureController {

    private final OrderCaptureService orderCaptureService;

    public OrderCaptureController(OrderCaptureService orderCaptureService) {
        this.orderCaptureService = orderCaptureService;
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Places a new order. Performs optimistic inventory check via Resilience4j circuit breaker.")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto dto) {
        OrderDto order = orderCaptureService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    @Operation(summary = "List all orders")
    public ResponseEntity<List<OrderDto>> listOrders() {
        return ResponseEntity.ok(orderCaptureService.listAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderCaptureService.getOrder(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all orders for a user")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderCaptureService.getOrdersByUser(userId));
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm an order")
    public ResponseEntity<OrderDto> confirmOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderCaptureService.confirmOrder(id));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderCaptureService.cancelOrder(id));
    }
}

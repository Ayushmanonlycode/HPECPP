package com.showdown.springboot.ordercaptureservice.controller;

import com.showdown.springboot.ordercaptureservice.dto.CreateOrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderDto;
import com.showdown.springboot.ordercaptureservice.service.OrderCaptureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderCaptureController {

    private final OrderCaptureService orderCaptureService;

    public OrderCaptureController(OrderCaptureService orderCaptureService) {
        this.orderCaptureService = orderCaptureService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto dto) {
        OrderDto order = orderCaptureService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> listOrders() {
        return ResponseEntity.ok(orderCaptureService.listAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderCaptureService.getOrder(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderCaptureService.getOrdersByUser(userId));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderDto> confirmOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderCaptureService.confirmOrder(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderCaptureService.cancelOrder(id));
    }
}

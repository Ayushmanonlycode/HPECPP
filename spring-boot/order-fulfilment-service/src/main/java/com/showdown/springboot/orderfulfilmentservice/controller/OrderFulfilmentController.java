package com.showdown.springboot.orderfulfilmentservice.controller;

import com.showdown.springboot.orderfulfilmentservice.dto.CreateFulfilmentDto;
import com.showdown.springboot.orderfulfilmentservice.dto.FulfilmentDto;
import com.showdown.springboot.orderfulfilmentservice.dto.ShipmentUpdateDto;
import com.showdown.springboot.orderfulfilmentservice.service.OrderFulfilmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fulfilments")
@Tag(name = "Fulfilments", description = "Order fulfilment lifecycle: processing, shipping, delivery")
public class OrderFulfilmentController {

    private final OrderFulfilmentService fulfilmentService;

    public OrderFulfilmentController(OrderFulfilmentService fulfilmentService) {
        this.fulfilmentService = fulfilmentService;
    }

    @PostMapping
    @Operation(summary = "Create a fulfilment record", description = "Initiates fulfilment for a confirmed order.")
    public ResponseEntity<FulfilmentDto> createFulfilment(
            @Valid @RequestBody CreateFulfilmentDto dto) {
        FulfilmentDto created = fulfilmentService.createFulfilment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<FulfilmentDto>> listFulfilments() {
        return ResponseEntity.ok(fulfilmentService.listAllFulfilments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FulfilmentDto> getFulfilment(@PathVariable UUID id) {
        return ResponseEntity.ok(fulfilmentService.getFulfilment(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<FulfilmentDto> getFulfilmentByOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(fulfilmentService.getFulfilmentByOrder(orderId));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<FulfilmentDto> markProcessing(@PathVariable UUID id) {
        return ResponseEntity.ok(fulfilmentService.markProcessing(id));
    }

    @PutMapping("/{id}/ship")
    @Operation(summary = "Mark as shipped", description = "Records tracking number and marks the fulfilment as shipped.")
    public ResponseEntity<FulfilmentDto> markShipped(@PathVariable UUID id,
                                                     @Valid @RequestBody ShipmentUpdateDto dto) {
        return ResponseEntity.ok(fulfilmentService.markShipped(id, dto));
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<FulfilmentDto> markDelivered(@PathVariable UUID id) {
        return ResponseEntity.ok(fulfilmentService.markDelivered(id));
    }

    @PutMapping("/{id}/fail")
    public ResponseEntity<FulfilmentDto> markFailed(@PathVariable UUID id) {
        return ResponseEntity.ok(fulfilmentService.markFailed(id));
    }
}

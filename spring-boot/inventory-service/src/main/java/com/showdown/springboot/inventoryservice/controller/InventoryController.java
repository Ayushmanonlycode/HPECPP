package com.showdown.springboot.inventoryservice.controller;

import com.showdown.springboot.inventoryservice.dto.InventoryDto;
import com.showdown.springboot.inventoryservice.dto.StockAdjustmentDto;
import com.showdown.springboot.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDto>> listAll() {
        return ResponseEntity.ok(inventoryService.listAllStock());
    }

    @GetMapping("/{sku}")
    public ResponseEntity<InventoryDto> getStock(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getStock(sku));
    }

    @PostMapping
    public ResponseEntity<InventoryDto> createStock(@Valid @RequestBody StockAdjustmentDto dto) {
        InventoryDto created = inventoryService.createStock(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{sku}/adjust")
    public ResponseEntity<InventoryDto> adjustStock(@PathVariable String sku,
                                                    @RequestBody Map<String, Integer> body) {
        int delta = body.getOrDefault("quantity", 0);
        return ResponseEntity.ok(inventoryService.adjustStock(sku, delta));
    }

    @PostMapping("/{sku}/reserve")
    public ResponseEntity<InventoryDto> reserveStock(@PathVariable String sku,
                                                     @RequestBody Map<String, Integer> body) {
        int quantity = body.getOrDefault("quantity", 0);
        return ResponseEntity.ok(inventoryService.reserveStock(sku, quantity));
    }

    @PostMapping("/{sku}/release")
    public ResponseEntity<InventoryDto> releaseReservation(@PathVariable String sku,
                                                           @RequestBody Map<String, Integer> body) {
        int quantity = body.getOrDefault("quantity", 0);
        return ResponseEntity.ok(inventoryService.releaseReservation(sku, quantity));
    }

    @GetMapping("/{sku}/availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(@PathVariable String sku,
                                                                  @RequestParam int quantity) {
        boolean available = inventoryService.checkAvailability(sku, quantity);
        return ResponseEntity.ok(Map.of("sku", sku, "quantity", quantity, "available", available));
    }
}

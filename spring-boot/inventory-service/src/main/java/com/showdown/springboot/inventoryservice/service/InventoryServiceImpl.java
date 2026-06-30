package com.showdown.springboot.inventoryservice.service;

import com.showdown.springboot.inventoryservice.dto.InventoryDto;
import com.showdown.springboot.inventoryservice.dto.StockAdjustmentDto;
import com.showdown.springboot.inventoryservice.entity.InventoryItem;
import com.showdown.springboot.inventoryservice.exception.InsufficientStockException;
import com.showdown.springboot.inventoryservice.exception.ResourceNotFoundException;
import com.showdown.springboot.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDto getStock(String sku) {
        InventoryItem item = inventoryRepository.findByItemSku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", sku));
        return toDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> listAllStock() {
        return inventoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryDto createStock(StockAdjustmentDto dto) {
        InventoryItem item = new InventoryItem(dto.getItemSku(), dto.getQuantity());
        InventoryItem saved = inventoryRepository.save(item);
        return toDto(saved);
    }

    @Override
    public InventoryDto adjustStock(String sku, int quantityDelta) {
        InventoryItem item = inventoryRepository.findByItemSkuForUpdate(sku)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", sku));

        int newQuantity = item.getQuantity() + quantityDelta;
        if (newQuantity < 0) {
            throw new InsufficientStockException(sku, Math.abs(quantityDelta), item.getQuantity());
        }

        item.setQuantity(newQuantity);
        InventoryItem saved = inventoryRepository.save(item);
        return toDto(saved);
    }

    @Override
    public InventoryDto reserveStock(String sku, int quantity) {
        InventoryItem item = inventoryRepository.findByItemSkuForUpdate(sku)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", sku));

        if (item.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException(sku, quantity, item.getAvailableQuantity());
        }

        item.setReservedQuantity(item.getReservedQuantity() + quantity);
        InventoryItem saved = inventoryRepository.save(item);
        return toDto(saved);
    }

    @Override
    public InventoryDto releaseReservation(String sku, int quantity) {
        InventoryItem item = inventoryRepository.findByItemSkuForUpdate(sku)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", sku));

        int newReserved = item.getReservedQuantity() - quantity;
        if (newReserved < 0) {
            newReserved = 0;
        }

        item.setReservedQuantity(newReserved);
        InventoryItem saved = inventoryRepository.save(item);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(String sku, int quantity) {
        return inventoryRepository.findByItemSku(sku)
                .map(item -> item.getAvailableQuantity() >= quantity)
                .orElse(false);
    }

    // ── Mapping helper ───────────────────────────────────────────

    private InventoryDto toDto(InventoryItem item) {
        return new InventoryDto(
                item.getId(),
                item.getItemSku(),
                item.getQuantity(),
                item.getReservedQuantity(),
                item.getAvailableQuantity(),
                item.getLastUpdated()
        );
    }
}

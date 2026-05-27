package com.showdown.springboot.inventoryservice.service;

import com.showdown.springboot.inventoryservice.dto.InventoryDto;
import com.showdown.springboot.inventoryservice.dto.StockAdjustmentDto;

import java.util.List;

public interface InventoryService {

    InventoryDto getStock(String sku);

    List<InventoryDto> listAllStock();

    InventoryDto createStock(StockAdjustmentDto dto);

    InventoryDto adjustStock(String sku, int quantityDelta);

    InventoryDto reserveStock(String sku, int quantity);

    InventoryDto releaseReservation(String sku, int quantity);

    boolean checkAvailability(String sku, int quantity);
}

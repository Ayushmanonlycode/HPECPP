package com.showdown.springboot.inventoryservice.service;

import com.showdown.springboot.inventoryservice.dto.InventoryDto;
import com.showdown.springboot.inventoryservice.dto.StockAdjustmentDto;
import com.showdown.springboot.inventoryservice.entity.InventoryItem;
import com.showdown.springboot.inventoryservice.exception.InsufficientStockException;
import com.showdown.springboot.inventoryservice.exception.ResourceNotFoundException;
import com.showdown.springboot.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryServiceImpl(inventoryRepository);
    }

    @Test
    void getStock_whenExists_shouldReturnDto() {
        InventoryItem item = new InventoryItem("EST-1", 100);
        item.setId(UUID.randomUUID());
        item.setReservedQuantity(10);

        when(inventoryRepository.findByItemSku("EST-1")).thenReturn(Optional.of(item));

        InventoryDto dto = inventoryService.getStock("EST-1");

        assertThat(dto).isNotNull();
        assertThat(dto.getItemSku()).isEqualTo("EST-1");
        assertThat(dto.getQuantity()).isEqualTo(100);
        assertThat(dto.getReservedQuantity()).isEqualTo(10);
        assertThat(dto.getAvailableQuantity()).isEqualTo(90);
    }

    @Test
    void getStock_whenNotExists_shouldThrowResourceNotFoundException() {
        when(inventoryRepository.findByItemSku("EST-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.getStock("EST-1"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void adjustStock_whenSufficient_shouldUpdateQuantity() {
        InventoryItem item = new InventoryItem("EST-1", 100);
        item.setId(UUID.randomUUID());

        when(inventoryRepository.findByItemSkuForUpdate("EST-1")).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryDto result = inventoryService.adjustStock("EST-1", 50);

        assertThat(result.getQuantity()).isEqualTo(150);
        verify(inventoryRepository, times(1)).save(item);
    }

    @Test
    void adjustStock_whenInsufficient_shouldThrowInsufficientStockException() {
        InventoryItem item = new InventoryItem("EST-1", 30);
        item.setId(UUID.randomUUID());

        when(inventoryRepository.findByItemSkuForUpdate("EST-1")).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> inventoryService.adjustStock("EST-1", -40))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("EST-1");
    }

    @Test
    void reserveStock_whenSufficient_shouldIncrementReservedQuantity() {
        InventoryItem item = new InventoryItem("EST-1", 100);
        item.setId(UUID.randomUUID());
        item.setReservedQuantity(10);

        when(inventoryRepository.findByItemSkuForUpdate("EST-1")).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryDto result = inventoryService.reserveStock("EST-1", 20);

        assertThat(result.getReservedQuantity()).isEqualTo(30);
        assertThat(result.getAvailableQuantity()).isEqualTo(70);
    }

    @Test
    void reserveStock_whenInsufficientAvailable_shouldThrowInsufficientStockException() {
        InventoryItem item = new InventoryItem("EST-1", 50);
        item.setId(UUID.randomUUID());
        item.setReservedQuantity(40); // 10 available

        when(inventoryRepository.findByItemSkuForUpdate("EST-1")).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> inventoryService.reserveStock("EST-1", 15))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void releaseReservation_shouldReduceReservedQuantity() {
        InventoryItem item = new InventoryItem("EST-1", 100);
        item.setId(UUID.randomUUID());
        item.setReservedQuantity(30);

        when(inventoryRepository.findByItemSkuForUpdate("EST-1")).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryDto result = inventoryService.releaseReservation("EST-1", 10);

        assertThat(result.getReservedQuantity()).isEqualTo(20);
        assertThat(result.getAvailableQuantity()).isEqualTo(80);
    }

    @Test
    void checkAvailability_shouldReturnBooleanBasedOnStock() {
        InventoryItem item = new InventoryItem("EST-1", 100);
        item.setId(UUID.randomUUID());
        item.setReservedQuantity(95); // 5 available

        when(inventoryRepository.findByItemSku("EST-1")).thenReturn(Optional.of(item));

        boolean available = inventoryService.checkAvailability("EST-1", 5);
        boolean unavailable = inventoryService.checkAvailability("EST-1", 6);

        assertThat(available).isTrue();
        assertThat(unavailable).isFalse();
    }
}

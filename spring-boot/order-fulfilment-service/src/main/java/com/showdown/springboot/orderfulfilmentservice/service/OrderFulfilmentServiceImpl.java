package com.showdown.springboot.orderfulfilmentservice.service;

import com.showdown.springboot.orderfulfilmentservice.dto.CreateFulfilmentDto;
import com.showdown.springboot.orderfulfilmentservice.dto.FulfilmentDto;
import com.showdown.springboot.orderfulfilmentservice.dto.ShipmentUpdateDto;
import com.showdown.springboot.orderfulfilmentservice.entity.Fulfilment;
import com.showdown.springboot.orderfulfilmentservice.entity.FulfilmentStatus;
import com.showdown.springboot.orderfulfilmentservice.exception.InvalidFulfilmentStateException;
import com.showdown.springboot.orderfulfilmentservice.exception.ResourceNotFoundException;
import com.showdown.springboot.orderfulfilmentservice.client.OrderCaptureClient;
import com.showdown.springboot.orderfulfilmentservice.repository.FulfilmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderFulfilmentServiceImpl implements OrderFulfilmentService {

    private final FulfilmentRepository fulfilmentRepository;
    private final OrderCaptureClient orderCaptureClient;

    public OrderFulfilmentServiceImpl(FulfilmentRepository fulfilmentRepository, OrderCaptureClient orderCaptureClient) {
        this.fulfilmentRepository = fulfilmentRepository;
        this.orderCaptureClient = orderCaptureClient;
    }

    @Override
    public FulfilmentDto createFulfilment(CreateFulfilmentDto dto) {
        Fulfilment fulfilment = new Fulfilment();
        fulfilment.setOrderId(dto.getOrderId());
        fulfilment.setStatus(FulfilmentStatus.PENDING);
        Fulfilment saved = fulfilmentRepository.save(fulfilment);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FulfilmentDto getFulfilment(UUID id) {
        Fulfilment fulfilment = fulfilmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfilment", id));
        return toDto(fulfilment);
    }

    @Override
    @Transactional(readOnly = true)
    public FulfilmentDto getFulfilmentByOrder(String orderId) {
        Fulfilment fulfilment = fulfilmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfilment for order", orderId));
        return toDto(fulfilment);
    }

    @Override
    public FulfilmentDto markProcessing(UUID id) {
        Fulfilment fulfilment = findAndValidateTransition(id, FulfilmentStatus.PENDING,
                FulfilmentStatus.PROCESSING);
        fulfilment.setStatus(FulfilmentStatus.PROCESSING);
        return toDto(fulfilmentRepository.save(fulfilment));
    }

    @Override
    public FulfilmentDto markShipped(UUID id, ShipmentUpdateDto dto) {
        Fulfilment fulfilment = findAndValidateTransition(id, FulfilmentStatus.PROCESSING,
                FulfilmentStatus.SHIPPED);
        fulfilment.setStatus(FulfilmentStatus.SHIPPED);
        fulfilment.setTrackingNumber(dto.getTrackingNumber());
        fulfilment.setCarrier(dto.getCarrier());
        fulfilment.setShippedAt(Instant.now());
        
        // Finalize order to deduct inventory
        orderCaptureClient.confirmOrder(fulfilment.getOrderId());
        
        return toDto(fulfilmentRepository.save(fulfilment));
    }

    @Override
    public FulfilmentDto markDelivered(UUID id) {
        Fulfilment fulfilment = findAndValidateTransition(id, FulfilmentStatus.SHIPPED,
                FulfilmentStatus.DELIVERED);
        fulfilment.setStatus(FulfilmentStatus.DELIVERED);
        fulfilment.setDeliveredAt(Instant.now());
        return toDto(fulfilmentRepository.save(fulfilment));
    }

    @Override
    public FulfilmentDto markFailed(UUID id) {
        Fulfilment fulfilment = fulfilmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfilment", id));

        if (fulfilment.getStatus() == FulfilmentStatus.DELIVERED) {
            throw new InvalidFulfilmentStateException(
                    fulfilment.getStatus().name(), FulfilmentStatus.FAILED.name());
        }

        fulfilment.setStatus(FulfilmentStatus.FAILED);
        return toDto(fulfilmentRepository.save(fulfilment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FulfilmentDto> listAllFulfilments() {
        return fulfilmentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ── Helpers ──────────────────────────────────────────────────

    private Fulfilment findAndValidateTransition(UUID id, FulfilmentStatus expectedCurrent,
                                                  FulfilmentStatus target) {
        Fulfilment fulfilment = fulfilmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfilment", id));

        if (fulfilment.getStatus() != expectedCurrent) {
            throw new InvalidFulfilmentStateException(
                    fulfilment.getStatus().name(), target.name());
        }

        return fulfilment;
    }

    private FulfilmentDto toDto(Fulfilment entity) {
        FulfilmentDto dto = new FulfilmentDto();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrderId());
        dto.setStatus(entity.getStatus().name());
        dto.setTrackingNumber(entity.getTrackingNumber());
        dto.setCarrier(entity.getCarrier());
        dto.setShippedAt(entity.getShippedAt());
        dto.setDeliveredAt(entity.getDeliveredAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}

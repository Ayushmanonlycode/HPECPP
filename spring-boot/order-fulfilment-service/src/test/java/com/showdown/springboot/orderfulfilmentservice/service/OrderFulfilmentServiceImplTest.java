package com.showdown.springboot.orderfulfilmentservice.service;

import com.showdown.springboot.orderfulfilmentservice.dto.CreateFulfilmentDto;
import com.showdown.springboot.orderfulfilmentservice.dto.FulfilmentDto;
import com.showdown.springboot.orderfulfilmentservice.dto.ShipmentUpdateDto;
import com.showdown.springboot.orderfulfilmentservice.entity.Fulfilment;
import com.showdown.springboot.orderfulfilmentservice.entity.FulfilmentStatus;
import com.showdown.springboot.orderfulfilmentservice.exception.InvalidFulfilmentStateException;
import com.showdown.springboot.orderfulfilmentservice.exception.ResourceNotFoundException;
import com.showdown.springboot.orderfulfilmentservice.repository.FulfilmentRepository;
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
public class OrderFulfilmentServiceImplTest {

    @Mock
    private FulfilmentRepository fulfilmentRepository;

    private OrderFulfilmentServiceImpl orderFulfilmentService;

    @BeforeEach
    void setUp() {
        orderFulfilmentService = new OrderFulfilmentServiceImpl(fulfilmentRepository);
    }

    @Test
    void createFulfilment_shouldSaveAsPending() {
        UUID orderId = UUID.randomUUID();
        CreateFulfilmentDto dto = new CreateFulfilmentDto();
        dto.setOrderId(orderId);

        when(fulfilmentRepository.save(any(Fulfilment.class))).thenAnswer(invocation -> {
            Fulfilment saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        FulfilmentDto result = orderFulfilmentService.createFulfilment(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getStatus()).isEqualTo(FulfilmentStatus.PENDING.name());
    }

    @Test
    void markProcessing_whenPending_shouldTransitionToProcessing() {
        UUID id = UUID.randomUUID();
        Fulfilment f = new Fulfilment();
        f.setId(id);
        f.setStatus(FulfilmentStatus.PENDING);

        when(fulfilmentRepository.findById(id)).thenReturn(Optional.of(f));
        when(fulfilmentRepository.save(any(Fulfilment.class))).thenReturn(f);

        FulfilmentDto result = orderFulfilmentService.markProcessing(id);

        assertThat(result.getStatus()).isEqualTo(FulfilmentStatus.PROCESSING.name());
    }

    @Test
    void markProcessing_whenNotPending_shouldThrowInvalidFulfilmentStateException() {
        UUID id = UUID.randomUUID();
        Fulfilment f = new Fulfilment();
        f.setId(id);
        f.setStatus(FulfilmentStatus.SHIPPED);

        when(fulfilmentRepository.findById(id)).thenReturn(Optional.of(f));

        assertThatThrownBy(() -> orderFulfilmentService.markProcessing(id))
                .isInstanceOf(InvalidFulfilmentStateException.class);
    }

    @Test
    void markShipped_whenProcessing_shouldTransitionToShippedAndSetDetails() {
        UUID id = UUID.randomUUID();
        Fulfilment f = new Fulfilment();
        f.setId(id);
        f.setStatus(FulfilmentStatus.PROCESSING);

        ShipmentUpdateDto dto = new ShipmentUpdateDto();
        dto.setCarrier("USPS");
        dto.setTrackingNumber("1Z99999");

        when(fulfilmentRepository.findById(id)).thenReturn(Optional.of(f));
        when(fulfilmentRepository.save(any(Fulfilment.class))).thenReturn(f);

        FulfilmentDto result = orderFulfilmentService.markShipped(id, dto);

        assertThat(result.getStatus()).isEqualTo(FulfilmentStatus.SHIPPED.name());
        assertThat(result.getCarrier()).isEqualTo("USPS");
        assertThat(result.getTrackingNumber()).isEqualTo("1Z99999");
        assertThat(result.getShippedAt()).isNotNull();
    }

    @Test
    void markDelivered_whenShipped_shouldTransitionToDelivered() {
        UUID id = UUID.randomUUID();
        Fulfilment f = new Fulfilment();
        f.setId(id);
        f.setStatus(FulfilmentStatus.SHIPPED);

        when(fulfilmentRepository.findById(id)).thenReturn(Optional.of(f));
        when(fulfilmentRepository.save(any(Fulfilment.class))).thenReturn(f);

        FulfilmentDto result = orderFulfilmentService.markDelivered(id);

        assertThat(result.getStatus()).isEqualTo(FulfilmentStatus.DELIVERED.name());
        assertThat(result.getDeliveredAt()).isNotNull();
    }

    @Test
    void markFailed_whenNotDelivered_shouldTransitionToFailed() {
        UUID id = UUID.randomUUID();
        Fulfilment f = new Fulfilment();
        f.setId(id);
        f.setStatus(FulfilmentStatus.PROCESSING);

        when(fulfilmentRepository.findById(id)).thenReturn(Optional.of(f));
        when(fulfilmentRepository.save(any(Fulfilment.class))).thenReturn(f);

        FulfilmentDto result = orderFulfilmentService.markFailed(id);

        assertThat(result.getStatus()).isEqualTo(FulfilmentStatus.FAILED.name());
    }

    @Test
    void markFailed_whenDelivered_shouldThrowException() {
        UUID id = UUID.randomUUID();
        Fulfilment f = new Fulfilment();
        f.setId(id);
        f.setStatus(FulfilmentStatus.DELIVERED);

        when(fulfilmentRepository.findById(id)).thenReturn(Optional.of(f));

        assertThatThrownBy(() -> orderFulfilmentService.markFailed(id))
                .isInstanceOf(InvalidFulfilmentStateException.class);
    }
}

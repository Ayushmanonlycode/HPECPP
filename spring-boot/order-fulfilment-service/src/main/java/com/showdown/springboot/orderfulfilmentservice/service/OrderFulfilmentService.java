package com.showdown.springboot.orderfulfilmentservice.service;

import com.showdown.springboot.orderfulfilmentservice.dto.CreateFulfilmentDto;
import com.showdown.springboot.orderfulfilmentservice.dto.FulfilmentDto;
import com.showdown.springboot.orderfulfilmentservice.dto.ShipmentUpdateDto;

import java.util.List;
import java.util.UUID;

public interface OrderFulfilmentService {

    FulfilmentDto createFulfilment(CreateFulfilmentDto dto);

    FulfilmentDto getFulfilment(UUID id);

    FulfilmentDto getFulfilmentByOrder(String orderId);

    FulfilmentDto markProcessing(UUID id);

    FulfilmentDto markShipped(UUID id, ShipmentUpdateDto dto);

    FulfilmentDto markDelivered(UUID id);

    FulfilmentDto markFailed(UUID id);

    List<FulfilmentDto> listAllFulfilments();
}

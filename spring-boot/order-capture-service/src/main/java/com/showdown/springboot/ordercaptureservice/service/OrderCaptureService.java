package com.showdown.springboot.ordercaptureservice.service;

import com.showdown.springboot.ordercaptureservice.dto.CreateOrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderCaptureService {

    OrderDto createOrder(CreateOrderDto dto);

    OrderDto getOrder(UUID id);

    List<OrderDto> getOrdersByUser(UUID userId);

    OrderDto confirmOrder(UUID id);

    OrderDto cancelOrder(UUID id);

    List<OrderDto> listAllOrders();
}

package com.showdown.springboot.ordercaptureservice.service;

import com.showdown.springboot.ordercaptureservice.dto.CreateOrderDto;
import com.showdown.springboot.ordercaptureservice.dto.OrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderCaptureService {

    OrderDto createOrder(CreateOrderDto dto);

    OrderDto getOrder(String id);

    List<OrderDto> getOrdersByUser(String userId);

    OrderDto confirmOrder(String id);

    OrderDto cancelOrder(String id);

    List<OrderDto> listAllOrders();
}

package com.showdown.wildfly.orderfulfillmentservice.Service;

import com.showdown.wildfly.orderfulfillmentservice.Models.OrderFulfillment;
import com.showdown.wildfly.orderfulfillmentservice.Repository.OrderFulfillmentRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class OrderFulfillmentService {

    @Inject
    OrderFulfillmentRepository repository;

    public void addOrder(OrderFulfillment order) {
        repository.addOrder(order);
    }

    public List<OrderFulfillment> getAllOrders() {
        return repository.getAllOrders();
    }
    
    public OrderFulfillment getOrderById(String orderId) {
        return repository.getOrderById(orderId);
    }

    public OrderFulfillment updateOrder(OrderFulfillment order) {
        return repository.updateOrder(order);
    }

    public void deleteOrder(String orderId) {
        repository.deleteOrder(orderId);
    }
    public List<OrderFulfillment> getOrdersByStatus(String status) {
        return repository.getOrdersByStatus(status);
    }
    public void shipOrder(String orderId) {
        repository.shipOrder(orderId);
    }

    public void deliverOrder(String orderId) {
        repository.deliverOrder(orderId);
    }

    public void cancelOrder(String orderId) {
        repository.cancelOrder(orderId);
    }
    public void processOrder(String orderId) {
        repository.processOrder(orderId);
    }
    public void failOrder(String orderId) {
        repository.failOrder(orderId);
    }
    public OrderFulfillment getByTrackingNumber(String trackingNumber) {
        return repository.getByTrackingNumber(trackingNumber);
    }
    public long countOrders() {
        return repository.countOrders();
    }
    public double getTotalRevenue() {
        return repository.getTotalRevenue();
    }
}
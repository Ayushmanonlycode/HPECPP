package com.showdown.springboot.ordercaptureservice.repository;

import com.showdown.springboot.ordercaptureservice.entity.Order;
import com.showdown.springboot.ordercaptureservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserId(String userId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
}

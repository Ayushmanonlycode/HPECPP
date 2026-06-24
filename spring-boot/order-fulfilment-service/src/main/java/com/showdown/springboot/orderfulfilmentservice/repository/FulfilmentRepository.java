package com.showdown.springboot.orderfulfilmentservice.repository;

import com.showdown.springboot.orderfulfilmentservice.entity.Fulfilment;
import com.showdown.springboot.orderfulfilmentservice.entity.FulfilmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FulfilmentRepository extends JpaRepository<Fulfilment, UUID> {

    Optional<Fulfilment> findByOrderId(String orderId);

    List<Fulfilment> findByStatus(FulfilmentStatus status);
}

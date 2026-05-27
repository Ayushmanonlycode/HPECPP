package com.showdown.springboot.catalogservice.repository;

import com.showdown.springboot.catalogservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByProductId(UUID productId);

    Optional<Item> findBySku(String sku);

    List<Item> findByDescriptionContainingIgnoreCase(String keyword);
}

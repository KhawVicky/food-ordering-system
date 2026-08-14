package com.foodordering.delivery.repository;

import com.foodordering.delivery.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {

    // Find a delivery by order ID.
    Optional<Delivery> findByOrderId(Long orderId);

    // Check if a delivery exists for an order.
    boolean existsByOrderId(Long orderId);

    // Find all deliveries from newest to oldest.
    List<Delivery> findAllByOrderByCreatedAtDesc();
}

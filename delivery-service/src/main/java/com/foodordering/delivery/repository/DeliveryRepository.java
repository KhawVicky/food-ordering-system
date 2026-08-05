package com.foodordering.delivery.repository;

import com.foodordering.delivery.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {

    Optional<Delivery> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

    List<Delivery> findAllByOrderByCreatedAtDesc();
}

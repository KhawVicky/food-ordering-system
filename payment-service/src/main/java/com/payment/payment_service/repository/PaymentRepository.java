package com.payment.payment_service.repository;

import com.payment.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    // Find a payment by order ID.
    Optional<Payment> findByOrderId(Long orderId);

    // Check if a payment exists for an order.
    boolean existsByOrderId(Long orderId);

    // Find all payments from newest to oldest.
    List<Payment> findAllByOrderByCreatedAtDesc();
}

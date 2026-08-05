package com.payment.payment_service.repository;

import com.payment.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

    List<Payment> findAllByOrderByCreatedAtDesc();
}

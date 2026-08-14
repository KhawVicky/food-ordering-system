package com.foodordering.order.repository;

import com.foodordering.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders for one customer.
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // Find all orders from newest to oldest.
    List<Order> findAllByOrderByCreatedAtDesc();
}

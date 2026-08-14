package com.foodordering.order.repository;

import com.foodordering.order.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email.
    Optional<User> findByEmailIgnoreCase(String email);

    // Check if an email is already used.
    boolean existsByEmailIgnoreCase(String email);
}

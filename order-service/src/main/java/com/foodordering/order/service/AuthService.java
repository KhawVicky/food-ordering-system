package com.foodordering.order.service;

import com.foodordering.order.dto.LoginRequest;
import com.foodordering.order.dto.LoginResponse;
import com.foodordering.order.dto.RegisterRequest;
import com.foodordering.order.exception.DuplicateEmailException;
import com.foodordering.order.exception.InvalidLoginException;
import com.foodordering.order.model.User;
import com.foodordering.order.model.UserRole;
import com.foodordering.order.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // Create the authentication service.
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Check the login details.
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
                .filter(candidate -> candidate.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new InvalidLoginException("Email or password is incorrect"));

        return LoginResponse.fromEntity(user);
    }

    // Register a new customer.
    public LoginResponse register(RegisterRequest request) {
        String name = request.getName().trim();
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException("An account with this email already exists");
        }

        User user = new User(name, email, request.getPassword(), UserRole.CUSTOMER);
        try {
            return LoginResponse.fromEntity(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateEmailException("An account with this email already exists");
        }
    }
}

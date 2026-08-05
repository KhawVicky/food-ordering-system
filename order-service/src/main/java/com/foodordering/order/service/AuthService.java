package com.foodordering.order.service;

import com.foodordering.order.dto.LoginRequest;
import com.foodordering.order.dto.LoginResponse;
import com.foodordering.order.exception.InvalidLoginException;
import com.foodordering.order.model.User;
import com.foodordering.order.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
                .filter(candidate -> candidate.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new InvalidLoginException("Email or password is incorrect"));

        return LoginResponse.fromEntity(user);
    }
}

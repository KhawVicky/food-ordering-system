package com.foodordering.order.controller;

import com.foodordering.order.dto.LoginRequest;
import com.foodordering.order.dto.LoginResponse;
import com.foodordering.order.dto.RegisterRequest;
import com.foodordering.order.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Create the authentication controller.
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Log in an existing user.
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // Create a new customer account.
    @PostMapping("/register")
    public LoginResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}

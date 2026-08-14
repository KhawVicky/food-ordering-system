package com.foodordering.order.dto;

import com.foodordering.order.model.User;
import com.foodordering.order.model.UserRole;

public class LoginResponse {

    private Long userId;
    private String name;
    private String email;
    private UserRole role;

    // Create an empty login response.
    public LoginResponse() {
    }

    // Build a response from a user.
    public static LoginResponse fromEntity(User user) {
        LoginResponse response = new LoginResponse();
        response.userId = user.getUserId();
        response.name = user.getName();
        response.email = user.getEmail();
        response.role = user.getRole();
        return response;
    }

    // Get the user ID.
    public Long getUserId() {
        return userId;
    }

    // Get the user name.
    public String getName() {
        return name;
    }

    // Get the email.
    public String getEmail() {
        return email;
    }

    // Get the user role.
    public UserRole getRole() {
        return role;
    }
}

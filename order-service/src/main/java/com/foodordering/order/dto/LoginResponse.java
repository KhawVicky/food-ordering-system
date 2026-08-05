package com.foodordering.order.dto;

import com.foodordering.order.model.User;
import com.foodordering.order.model.UserRole;

public class LoginResponse {

    private Long userId;
    private String name;
    private String email;
    private UserRole role;

    public LoginResponse() {
    }

    public static LoginResponse fromEntity(User user) {
        LoginResponse response = new LoginResponse();
        response.userId = user.getUserId();
        response.name = user.getName();
        response.email = user.getEmail();
        response.role = user.getRole();
        return response;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}

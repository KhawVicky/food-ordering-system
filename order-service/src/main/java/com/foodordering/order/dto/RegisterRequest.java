package com.foodordering.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be 100 characters or fewer")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must be 255 characters or fewer")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    // Create an empty register request.
    public RegisterRequest() {
    }

    // Create a register request with user details.
    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Get the name.
    public String getName() {
        return name;
    }

    // Set the name.
    public void setName(String name) {
        this.name = name;
    }

    // Get the email.
    public String getEmail() {
        return email;
    }

    // Set the email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Get the password.
    public String getPassword() {
        return password;
    }

    // Set the password.
    public void setPassword(String password) {
        this.password = password;
    }
}

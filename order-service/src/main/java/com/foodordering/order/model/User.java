package com.foodordering.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Create an empty user for JPA.
    protected User() {
    }

    // Create a user with account details.
    public User(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Get the user ID.
    public Long getUserId() {
        return userId;
    }

    // Set the user ID.
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Get the user name.
    public String getName() {
        return name;
    }

    // Set the user name.
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

    // Get the user role.
    public UserRole getRole() {
        return role;
    }

    // Set the user role.
    public void setRole(UserRole role) {
        this.role = role;
    }
}

package com.foodordering.order.exception;

public class InvalidLoginException extends RuntimeException {

    // Create a login error.
    public InvalidLoginException(String message) {
        super(message);
    }
}

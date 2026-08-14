package com.foodordering.order.exception;

public class InvalidStatusTransitionException extends RuntimeException {

    // Create a status change error.
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}

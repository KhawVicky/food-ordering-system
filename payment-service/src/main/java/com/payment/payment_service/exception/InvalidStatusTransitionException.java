package com.payment.payment_service.exception;

public class InvalidStatusTransitionException extends RuntimeException {

    // Create a status change error.
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}

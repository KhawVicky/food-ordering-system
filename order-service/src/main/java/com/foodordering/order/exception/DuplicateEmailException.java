package com.foodordering.order.exception;

public class DuplicateEmailException extends RuntimeException {

    // Create a duplicate email error.
    public DuplicateEmailException(String message) {
        super(message);
    }
}

package com.foodordering.delivery.exception;

public class DuplicateDeliveryException extends RuntimeException {

    // Create a duplicate delivery error.
    public DuplicateDeliveryException(String message) {
        super(message);
    }
}

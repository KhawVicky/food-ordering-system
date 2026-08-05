package com.foodordering.delivery.exception;

public class DuplicateDeliveryException extends RuntimeException {

    public DuplicateDeliveryException(String message) {
        super(message);
    }
}

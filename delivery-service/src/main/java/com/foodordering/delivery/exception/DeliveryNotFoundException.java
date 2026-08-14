package com.foodordering.delivery.exception;

public class DeliveryNotFoundException extends RuntimeException {

    // Create a delivery not found error.
    public DeliveryNotFoundException(String message) {
        super(message);
    }
}

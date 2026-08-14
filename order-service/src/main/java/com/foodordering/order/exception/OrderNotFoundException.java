package com.foodordering.order.exception;

public class OrderNotFoundException extends RuntimeException {

    // Create an order not found error.
    public OrderNotFoundException(String message) {
        super(message);
    }
}

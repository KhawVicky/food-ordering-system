package com.payment.payment_service.exception;

public class PaymentNotFoundException extends RuntimeException {

    // Create a payment not found error.
    public PaymentNotFoundException(String message) {
        super(message);
    }
}

package com.payment.payment_service.exception;

public class DuplicatePaymentException extends RuntimeException {

    // Create a duplicate payment error.
    public DuplicatePaymentException(String message) {
        super(message);
    }
}

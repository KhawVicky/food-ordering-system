package com.payment.payment_service.dto;

import com.payment.payment_service.model.PaymentStatus;

public class PaymentStatusUpdateRequest {

    private PaymentStatus status;
    private PaymentStatus paymentStatus;

    // Create an empty status request.
    public PaymentStatusUpdateRequest() {
    }

    // Create a status request with a status.
    public PaymentStatusUpdateRequest(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Get the status.
    public PaymentStatus getStatus() {
        return status;
    }

    // Set the status.
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    // Get the payment status.
    public PaymentStatus getPaymentStatus() {
        return status != null ? status : paymentStatus;
    }

    // Set the payment status.
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

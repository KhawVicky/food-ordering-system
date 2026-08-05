package com.payment.payment_service.dto;

import com.payment.payment_service.model.PaymentStatus;

public class PaymentStatusUpdateRequest {

    private PaymentStatus status;
    private PaymentStatus paymentStatus;

    public PaymentStatusUpdateRequest() {
    }

    public PaymentStatusUpdateRequest(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return status != null ? status : paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

package com.payment.payment_service.dto;

import com.payment.payment_service.model.Payment;
import com.payment.payment_service.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private String paymentId;
    private Long orderId;
    private Long customerId;
    private BigDecimal amount;
    private String paymentMethod;
    private String deliveryAddress;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    // Create an empty payment response.
    public PaymentResponse() {
    }

    // Build a response from a payment.
    public static PaymentResponse fromEntity(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setOrderId(payment.getOrderId());
        response.setCustomerId(payment.getCustomerId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setDeliveryAddress(payment.getDeliveryAddress());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setCreatedAt(payment.getCreatedAt());
        response.setPaidAt(payment.getPaidAt());
        return response;
    }

    // Get the payment ID.
    public String getPaymentId() {
        return paymentId;
    }

    // Set the payment ID.
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    // Get the order ID.
    public Long getOrderId() {
        return orderId;
    }

    // Set the order ID.
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    // Get the customer ID.
    public Long getCustomerId() {
        return customerId;
    }

    // Set the customer ID.
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // Get the payment amount.
    public BigDecimal getAmount() {
        return amount;
    }

    // Set the payment amount.
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Get the payment method.
    public String getPaymentMethod() {
        return paymentMethod;
    }

    // Set the payment method.
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Get the delivery address.
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Set the delivery address.
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    // Get the payment status.
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    // Set the payment status.
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Get the creation time.
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Set the creation time.
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Get the paid time.
    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    // Set the paid time.
    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}

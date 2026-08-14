package com.foodordering.order.messaging.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentCompletedEvent {

    private String paymentId;
    private Long orderId;
    private Long customerId;
    private BigDecimal paidAmount;
    private String paymentStatus;
    private String deliveryAddress;
    private LocalDateTime paidAt;

    // Create an empty payment event.
    public PaymentCompletedEvent() {
    }

    // Create a payment event with payment details.
    public PaymentCompletedEvent(
            String paymentId,
            Long orderId,
            Long customerId,
            BigDecimal paidAmount,
            String paymentStatus,
            String deliveryAddress,
            LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.paidAmount = paidAmount;
        this.paymentStatus = paymentStatus;
        this.deliveryAddress = deliveryAddress;
        this.paidAt = paidAt;
    }

    // Check if the payment is complete.
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(paymentStatus);
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

    // Get the paid amount.
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    // Set the paid amount.
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    // Get the payment status.
    public String getPaymentStatus() {
        return paymentStatus;
    }

    // Set the payment status.
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Get the delivery address.
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Set the delivery address.
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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

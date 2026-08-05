package com.foodordering.delivery.messaging.event;

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

    public PaymentCompletedEvent() {
    }

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

    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(paymentStatus);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}

package com.payment.payment_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Column(nullable = false)
    private Long orderId;

    private Long customerId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod;

    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime paidAt;

    // Create an empty payment for JPA.
    public Payment() {
    }

    // Create a pending payment.
    public Payment(Long orderId, BigDecimal amount, String paymentMethod) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    // Create a payment with customer details.
    public Payment(
            Long orderId,
            Long customerId,
            BigDecimal amount,
            String paymentMethod,
            String deliveryAddress) {
        this(orderId, amount, paymentMethod);
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
    }

    // Mark the payment as complete.
    public void completePayment() {
        this.paymentStatus = PaymentStatus.COMPLETED;
        if (this.paidAt == null) {
            this.paidAt = LocalDateTime.now();
        }
    }

    // Mark the payment as failed.
    public void failPayment() {
        this.paymentStatus = PaymentStatus.FAILED;
        this.paidAt = null;
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

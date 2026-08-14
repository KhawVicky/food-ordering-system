package com.payment.payment_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderCreatedEvent {

    private Long orderId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private BigDecimal totalAmount;
    private BigDecimal amount;
    private String paymentMethod;
    private String deliveryAddress;
    private LocalDateTime createdAt;

    // Create an empty order event.
    public OrderCreatedEvent() {
    }

    // Create an order event with payment data.
    public OrderCreatedEvent(Long orderId, BigDecimal amount, String paymentMethod) {
        this.orderId = orderId;
        this.totalAmount = amount;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    // Get the total amount used for payment.
    public BigDecimal getEffectiveTotalAmount() {
        return totalAmount != null ? totalAmount : amount;
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

    // Get the customer name.
    public String getCustomerName() {
        return customerName;
    }

    // Set the customer name.
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Get the customer email.
    public String getCustomerEmail() {
        return customerEmail;
    }

    // Set the customer email.
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    // Get the total amount.
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    // Set the total amount.
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Get the old amount field.
    public BigDecimal getAmount() {
        return amount != null ? amount : totalAmount;
    }

    // Set the old amount field.
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

    // Get the creation time.
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Set the creation time.
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

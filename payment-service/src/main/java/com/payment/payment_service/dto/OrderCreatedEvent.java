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

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, BigDecimal amount, String paymentMethod) {
        this.orderId = orderId;
        this.totalAmount = amount;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public BigDecimal getEffectiveTotalAmount() {
        return totalAmount != null ? totalAmount : amount;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : totalAmount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

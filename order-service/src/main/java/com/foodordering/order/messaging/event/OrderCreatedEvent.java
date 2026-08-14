package com.foodordering.order.messaging.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderCreatedEvent {

    private Long orderId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String deliveryAddress;
    private LocalDateTime createdAt;

    // Create an empty order event.
    public OrderCreatedEvent() {
    }

    // Create an order event with order details.
    public OrderCreatedEvent(
            Long orderId,
            Long customerId,
            String customerName,
            String customerEmail,
            BigDecimal totalAmount,
            String paymentMethod,
            String deliveryAddress,
            LocalDateTime createdAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = createdAt;
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

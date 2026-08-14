package com.foodordering.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String foodItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Create an empty order for JPA.
    protected Order() {
    }

    // Create a new pending order.
    public Order(
            Long customerId,
            String customerName,
            String customerEmail,
            String foodItem,
            Integer quantity,
            BigDecimal totalAmount,
            String paymentMethod,
            String deliveryAddress) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.orderStatus = OrderStatus.PENDING_PAYMENT;
        this.createdAt = LocalDateTime.now();
    }

    // Mark the order as paid.
    public void markAsPaid() {
        this.orderStatus = OrderStatus.PAID;
    }

    // Cancel the order.
    public void cancel() {
        this.orderStatus = OrderStatus.CANCELLED;
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

    // Get the food item.
    public String getFoodItem() {
        return foodItem;
    }

    // Set the food item.
    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    // Get the quantity.
    public Integer getQuantity() {
        return quantity;
    }

    // Set the quantity.
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    // Get the order status.
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    // Set the order status.
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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

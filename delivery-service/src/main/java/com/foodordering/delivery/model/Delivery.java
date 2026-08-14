package com.foodordering.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String deliveryId;

    @Column(nullable = false, unique = true)
    private Long orderId;

    @Column(nullable = false)
    private String paymentId;

    private Long customerId;

    @Column(nullable = false)
    private String deliveryAddress;

    private String riderName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    // Create an empty delivery for JPA.
    protected Delivery() {
    }

    // Create a pending delivery.
    public Delivery(String paymentId, Long orderId, Long customerId, String deliveryAddress) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryStatus = DeliveryStatus.PENDING_ASSIGNMENT;
        this.createdAt = LocalDateTime.now();
    }

    // Assign a rider to the delivery.
    public void assignRider(String riderName) {
        this.riderName = riderName;
        this.deliveryStatus = DeliveryStatus.RIDER_ASSIGNED;
    }

    // Mark the delivery as picked up.
    public void markAsPickedUp() {
        this.deliveryStatus = DeliveryStatus.PICKED_UP;
    }

    // Mark the delivery as delivered.
    public void markAsDelivered() {
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        if (this.deliveredAt == null) {
            this.deliveredAt = LocalDateTime.now();
        }
    }

    // Get the delivery ID.
    public String getDeliveryId() {
        return deliveryId;
    }

    // Set the delivery ID.
    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    // Get the order ID.
    public Long getOrderId() {
        return orderId;
    }

    // Set the order ID.
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    // Get the payment ID.
    public String getPaymentId() {
        return paymentId;
    }

    // Set the payment ID.
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    // Get the customer ID.
    public Long getCustomerId() {
        return customerId;
    }

    // Set the customer ID.
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // Get the delivery address.
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Set the delivery address.
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    // Get the rider name.
    public String getRiderName() {
        return riderName;
    }

    // Set the rider name.
    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    // Get the delivery status.
    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    // Set the delivery status.
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    // Get the creation time.
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Set the creation time.
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Get the delivered time.
    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    // Set the delivered time.
    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}

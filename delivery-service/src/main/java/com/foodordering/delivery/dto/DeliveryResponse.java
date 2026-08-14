package com.foodordering.delivery.dto;

import com.foodordering.delivery.model.Delivery;
import com.foodordering.delivery.model.DeliveryStatus;

import java.time.LocalDateTime;

public class DeliveryResponse {

    private String deliveryId;
    private Long orderId;
    private String paymentId;
    private Long customerId;
    private String deliveryAddress;
    private String riderName;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;

    // Create an empty delivery response.
    public DeliveryResponse() {
    }

    // Build a response from a delivery.
    public static DeliveryResponse fromEntity(Delivery delivery) {
        DeliveryResponse response = new DeliveryResponse();
        response.deliveryId = delivery.getDeliveryId();
        response.orderId = delivery.getOrderId();
        response.paymentId = delivery.getPaymentId();
        response.customerId = delivery.getCustomerId();
        response.deliveryAddress = delivery.getDeliveryAddress();
        response.riderName = delivery.getRiderName();
        response.deliveryStatus = delivery.getDeliveryStatus();
        response.createdAt = delivery.getCreatedAt();
        response.deliveredAt = delivery.getDeliveredAt();
        return response;
    }

    // Get the delivery ID.
    public String getDeliveryId() {
        return deliveryId;
    }

    // Get the order ID.
    public Long getOrderId() {
        return orderId;
    }

    // Get the payment ID.
    public String getPaymentId() {
        return paymentId;
    }

    // Get the customer ID.
    public Long getCustomerId() {
        return customerId;
    }

    // Get the delivery address.
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Get the rider name.
    public String getRiderName() {
        return riderName;
    }

    // Get the delivery status.
    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    // Get the creation time.
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Get the delivered time.
    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }
}

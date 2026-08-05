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

    public DeliveryResponse() {
    }

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

    public String getDeliveryId() {
        return deliveryId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getRiderName() {
        return riderName;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }
}

package com.foodordering.order.dto;

import com.foodordering.order.model.Order;
import com.foodordering.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {

    private Long orderId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String foodItem;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String deliveryAddress;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public static OrderResponse fromEntity(Order order) {
        OrderResponse response = new OrderResponse();
        response.orderId = order.getOrderId();
        response.customerId = order.getCustomerId();
        response.customerName = order.getCustomerName();
        response.customerEmail = order.getCustomerEmail();
        response.foodItem = order.getFoodItem();
        response.quantity = order.getQuantity();
        response.totalAmount = order.getTotalAmount();
        response.paymentMethod = order.getPaymentMethod();
        response.deliveryAddress = order.getDeliveryAddress();
        response.orderStatus = order.getOrderStatus();
        response.createdAt = order.getCreatedAt();
        return response;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

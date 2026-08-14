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

    // Create an empty order response.
    public OrderResponse() {
    }

    // Build a response from an order.
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

    // Get the order ID.
    public Long getOrderId() {
        return orderId;
    }

    // Get the customer ID.
    public Long getCustomerId() {
        return customerId;
    }

    // Get the customer name.
    public String getCustomerName() {
        return customerName;
    }

    // Get the customer email.
    public String getCustomerEmail() {
        return customerEmail;
    }

    // Get the food item.
    public String getFoodItem() {
        return foodItem;
    }

    // Get the quantity.
    public Integer getQuantity() {
        return quantity;
    }

    // Get the total amount.
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    // Get the payment method.
    public String getPaymentMethod() {
        return paymentMethod;
    }

    // Get the delivery address.
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Get the order status.
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    // Get the creation time.
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

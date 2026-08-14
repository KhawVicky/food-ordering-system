package com.foodordering.order.dto;

import com.foodordering.order.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {

    @NotNull(message = "Order status is required")
    private OrderStatus status;

    // Create an empty status request.
    public OrderStatusUpdateRequest() {
    }

    // Get the order status.
    public OrderStatus getStatus() {
        return status;
    }

    // Set the order status.
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

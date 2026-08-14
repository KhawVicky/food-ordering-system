package com.foodordering.delivery.dto;

import com.foodordering.delivery.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public class DeliveryStatusUpdateRequest {

    @NotNull(message = "Delivery status is required")
    private DeliveryStatus status;

    // Create an empty status request.
    public DeliveryStatusUpdateRequest() {
    }

    // Get the delivery status.
    public DeliveryStatus getStatus() {
        return status;
    }

    // Set the delivery status.
    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}

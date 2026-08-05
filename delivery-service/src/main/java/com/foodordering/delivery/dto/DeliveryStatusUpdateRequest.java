package com.foodordering.delivery.dto;

import com.foodordering.delivery.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public class DeliveryStatusUpdateRequest {

    @NotNull(message = "Delivery status is required")
    private DeliveryStatus status;

    public DeliveryStatusUpdateRequest() {
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}

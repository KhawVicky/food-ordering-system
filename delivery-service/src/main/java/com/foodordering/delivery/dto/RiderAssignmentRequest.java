package com.foodordering.delivery.dto;

import jakarta.validation.constraints.NotBlank;

public class RiderAssignmentRequest {

    @NotBlank(message = "Rider name is required")
    private String riderName;

    // Create an empty rider request.
    public RiderAssignmentRequest() {
    }

    // Get the rider name.
    public String getRiderName() {
        return riderName;
    }

    // Set the rider name.
    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }
}

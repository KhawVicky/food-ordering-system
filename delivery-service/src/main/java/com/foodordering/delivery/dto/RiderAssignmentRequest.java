package com.foodordering.delivery.dto;

import jakarta.validation.constraints.NotBlank;

public class RiderAssignmentRequest {

    @NotBlank(message = "Rider name is required")
    private String riderName;

    public RiderAssignmentRequest() {
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }
}

package com.foodordering.delivery.controller;

import com.foodordering.delivery.dto.DeliveryResponse;
import com.foodordering.delivery.dto.DeliveryStatusUpdateRequest;
import com.foodordering.delivery.dto.RiderAssignmentRequest;
import com.foodordering.delivery.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // Create the delivery controller.
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    // Get all deliveries.
    @GetMapping
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    // Get one delivery by ID.
    @GetMapping("/{deliveryId}")
    public DeliveryResponse getDeliveryById(@PathVariable String deliveryId) {
        return deliveryService.getDeliveryById(deliveryId);
    }

    // Get a delivery by order ID.
    @GetMapping("/order/{orderId}")
    public DeliveryResponse getDeliveryByOrderId(@PathVariable Long orderId) {
        return deliveryService.getDeliveryByOrderId(orderId);
    }

    // Assign a rider to a delivery.
    @PutMapping("/{deliveryId}/assign-rider")
    public DeliveryResponse assignRider(
            @PathVariable String deliveryId,
            @Valid @RequestBody RiderAssignmentRequest request) {
        return deliveryService.assignRider(deliveryId, request);
    }

    // Change the delivery status.
    @PutMapping("/{deliveryId}/status")
    public DeliveryResponse updateDeliveryStatus(
            @PathVariable String deliveryId,
            @Valid @RequestBody DeliveryStatusUpdateRequest request) {
        return deliveryService.updateDeliveryStatus(deliveryId, request);
    }
}

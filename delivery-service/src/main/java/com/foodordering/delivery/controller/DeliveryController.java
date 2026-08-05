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

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("/{deliveryId}")
    public DeliveryResponse getDeliveryById(@PathVariable String deliveryId) {
        return deliveryService.getDeliveryById(deliveryId);
    }

    @GetMapping("/order/{orderId}")
    public DeliveryResponse getDeliveryByOrderId(@PathVariable Long orderId) {
        return deliveryService.getDeliveryByOrderId(orderId);
    }

    @PutMapping("/{deliveryId}/assign-rider")
    public DeliveryResponse assignRider(
            @PathVariable String deliveryId,
            @Valid @RequestBody RiderAssignmentRequest request) {
        return deliveryService.assignRider(deliveryId, request);
    }

    @PutMapping("/{deliveryId}/status")
    public DeliveryResponse updateDeliveryStatus(
            @PathVariable String deliveryId,
            @Valid @RequestBody DeliveryStatusUpdateRequest request) {
        return deliveryService.updateDeliveryStatus(deliveryId, request);
    }
}

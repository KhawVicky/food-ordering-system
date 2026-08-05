package com.foodordering.delivery.service;

import com.foodordering.delivery.dto.DeliveryResponse;
import com.foodordering.delivery.dto.DeliveryStatusUpdateRequest;
import com.foodordering.delivery.dto.RiderAssignmentRequest;
import com.foodordering.delivery.exception.DuplicateDeliveryException;
import com.foodordering.delivery.exception.DeliveryNotFoundException;
import com.foodordering.delivery.exception.InvalidStatusTransitionException;
import com.foodordering.delivery.messaging.event.PaymentCompletedEvent;
import com.foodordering.delivery.model.Delivery;
import com.foodordering.delivery.model.DeliveryStatus;
import com.foodordering.delivery.repository.DeliveryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public DeliveryResponse createDelivery(PaymentCompletedEvent event) {
        validatePaymentEvent(event);

        Delivery existingDelivery = deliveryRepository.findByOrderId(event.getOrderId()).orElse(null);
        if (existingDelivery != null) {
            return DeliveryResponse.fromEntity(existingDelivery);
        }

        Delivery delivery = new Delivery(
                event.getPaymentId(),
                event.getOrderId(),
                event.getCustomerId(),
                event.getDeliveryAddress().trim()
        );

        try {
            return DeliveryResponse.fromEntity(deliveryRepository.save(delivery));
        } catch (DataIntegrityViolationException exception) {
            return deliveryRepository.findByOrderId(event.getOrderId())
                    .map(DeliveryResponse::fromEntity)
                    .orElseThrow(() -> new DuplicateDeliveryException(
                            "A delivery already exists for order: " + event.getOrderId()));
        }
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(DeliveryResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public DeliveryResponse getDeliveryById(String deliveryId) {
        return DeliveryResponse.fromEntity(findDelivery(deliveryId));
    }

    @Transactional(readOnly = true)
    public DeliveryResponse getDeliveryByOrderId(Long orderId) {
        return DeliveryResponse.fromEntity(deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DeliveryNotFoundException(
                        "Delivery not found for order: " + orderId)));
    }

    @Transactional
    public DeliveryResponse assignRider(String deliveryId, RiderAssignmentRequest request) {
        Delivery delivery = findDelivery(deliveryId);
        if (request == null || request.getRiderName() == null || request.getRiderName().isBlank()) {
            throw new IllegalArgumentException("Rider name is required");
        }
        if (delivery.getDeliveryStatus() == DeliveryStatus.RIDER_ASSIGNED
                && request.getRiderName().trim().equals(delivery.getRiderName())) {
            return DeliveryResponse.fromEntity(delivery);
        }
        if (delivery.getDeliveryStatus() != DeliveryStatus.PENDING_ASSIGNMENT) {
            throw new InvalidStatusTransitionException(
                    "Cannot assign a rider when delivery status is " + delivery.getDeliveryStatus());
        }

        delivery.assignRider(request.getRiderName().trim());
        return DeliveryResponse.fromEntity(deliveryRepository.save(delivery));
    }

    @Transactional
    public DeliveryResponse updateDeliveryStatus(
            String deliveryId,
            DeliveryStatusUpdateRequest request) {
        Delivery delivery = findDelivery(deliveryId);
        DeliveryStatus requestedStatus = request == null ? null : request.getStatus();
        if (requestedStatus == null) {
            throw new IllegalArgumentException("Delivery status is required");
        }
        if (requestedStatus == delivery.getDeliveryStatus()) {
            return DeliveryResponse.fromEntity(delivery);
        }

        switch (delivery.getDeliveryStatus()) {
            case PENDING_ASSIGNMENT -> {
                if (requestedStatus != DeliveryStatus.RIDER_ASSIGNED) {
                    throw invalidTransition(delivery, requestedStatus);
                }
                if (delivery.getRiderName() == null || delivery.getRiderName().isBlank()) {
                    throw invalidTransition(delivery, requestedStatus);
                }
                delivery.assignRider(delivery.getRiderName());
            }
            case RIDER_ASSIGNED -> {
                if (requestedStatus != DeliveryStatus.PICKED_UP) {
                    throw invalidTransition(delivery, requestedStatus);
                }
                delivery.markAsPickedUp();
            }
            case PICKED_UP -> {
                if (requestedStatus != DeliveryStatus.DELIVERED) {
                    throw invalidTransition(delivery, requestedStatus);
                }
                delivery.markAsDelivered();
            }
            case DELIVERED -> throw invalidTransition(delivery, requestedStatus);
        }

        return DeliveryResponse.fromEntity(deliveryRepository.save(delivery));
    }

    private Delivery findDelivery(String deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found: " + deliveryId));
    }

    private void validatePaymentEvent(PaymentCompletedEvent event) {
        if (event == null || !event.isCompleted()) {
            throw new IllegalArgumentException("Only completed payments create delivery tasks");
        }
        if (event.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID is required");
        }
        if (event.getPaymentId() == null || event.getPaymentId().isBlank()) {
            throw new IllegalArgumentException("Payment ID is required");
        }
        if (event.getDeliveryAddress() == null || event.getDeliveryAddress().isBlank()) {
            throw new IllegalArgumentException("Delivery address is required");
        }
    }

    private InvalidStatusTransitionException invalidTransition(
            Delivery delivery,
            DeliveryStatus requestedStatus) {
        return new InvalidStatusTransitionException(
                "Cannot change delivery status from " + delivery.getDeliveryStatus()
                        + " to " + requestedStatus);
    }
}

package com.foodordering.delivery;

import com.foodordering.delivery.dto.DeliveryStatusUpdateRequest;
import com.foodordering.delivery.dto.RiderAssignmentRequest;
import com.foodordering.delivery.exception.InvalidStatusTransitionException;
import com.foodordering.delivery.messaging.event.PaymentCompletedEvent;
import com.foodordering.delivery.model.Delivery;
import com.foodordering.delivery.model.DeliveryStatus;
import com.foodordering.delivery.repository.DeliveryRepository;
import com.foodordering.delivery.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceUnitTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryService(deliveryRepository);
    }

    @Test
    void completedPaymentCreatesOnlyOneDelivery() {
        PaymentCompletedEvent event = event();
        when(deliveryRepository.findByOrderId(11L))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(savedDelivery()));
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery delivery = invocation.getArgument(0);
            delivery.setDeliveryId("delivery-11");
            return delivery;
        });

        assertEquals(DeliveryStatus.PENDING_ASSIGNMENT,
                deliveryService.createDelivery(event).getDeliveryStatus());
        deliveryService.createDelivery(event);

        verify(deliveryRepository).save(any(Delivery.class));
    }

    @Test
    void riderCanBeAssignedAndDeliveryCanBeCompleted() {
        Delivery delivery = savedDelivery();
        when(deliveryRepository.findById("delivery-11")).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RiderAssignmentRequest riderRequest = new RiderAssignmentRequest();
        riderRequest.setRiderName("Ali");
        deliveryService.assignRider("delivery-11", riderRequest);

        DeliveryStatusUpdateRequest pickedUp = new DeliveryStatusUpdateRequest();
        pickedUp.setStatus(DeliveryStatus.PICKED_UP);
        deliveryService.updateDeliveryStatus("delivery-11", pickedUp);

        DeliveryStatusUpdateRequest delivered = new DeliveryStatusUpdateRequest();
        delivered.setStatus(DeliveryStatus.DELIVERED);
        deliveryService.updateDeliveryStatus("delivery-11", delivered);

        assertEquals(DeliveryStatus.DELIVERED, delivery.getDeliveryStatus());
        assertNotNull(delivery.getDeliveredAt());
    }

    @Test
    void pickedUpBeforeRiderAssignmentIsRejected() {
        Delivery delivery = savedDelivery();
        when(deliveryRepository.findById("delivery-11")).thenReturn(Optional.of(delivery));
        DeliveryStatusUpdateRequest request = new DeliveryStatusUpdateRequest();
        request.setStatus(DeliveryStatus.PICKED_UP);

        assertThrows(InvalidStatusTransitionException.class,
                () -> deliveryService.updateDeliveryStatus("delivery-11", request));
    }

    @Test
    void deliveredBeforePickedUpIsRejected() {
        Delivery delivery = savedDelivery();
        delivery.assignRider("Ali");
        when(deliveryRepository.findById("delivery-11")).thenReturn(Optional.of(delivery));
        DeliveryStatusUpdateRequest request = new DeliveryStatusUpdateRequest();
        request.setStatus(DeliveryStatus.DELIVERED);

        assertThrows(InvalidStatusTransitionException.class,
                () -> deliveryService.updateDeliveryStatus("delivery-11", request));
    }

    private PaymentCompletedEvent event() {
        return new PaymentCompletedEvent(
                "payment-11", 11L, 1L, new BigDecimal("20.00"), "COMPLETED", "Penang", null);
    }

    private Delivery savedDelivery() {
        Delivery delivery = new Delivery("payment-11", 11L, 1L, "Penang");
        delivery.setDeliveryId("delivery-11");
        return delivery;
    }
}

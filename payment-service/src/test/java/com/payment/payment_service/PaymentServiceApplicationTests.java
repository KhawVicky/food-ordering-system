package com.payment.payment_service;

import com.payment.payment_service.dto.OrderCreatedEvent;
import com.payment.payment_service.dto.PaymentStatusUpdateRequest;
import com.payment.payment_service.messaging.event.PaymentCompletedEvent;
import com.payment.payment_service.messaging.producer.PaymentEventProducer;
import com.payment.payment_service.model.Payment;
import com.payment.payment_service.model.PaymentStatus;
import com.payment.payment_service.repository.PaymentRepository;
import com.payment.payment_service.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceApplicationTests {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    private PaymentService paymentService;

    // Create the service before each test.
    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, paymentEventProducer);
    }

    // Test that one order creates one payment.
    @Test
    void orderCreatedEventCreatesOnlyOnePayment() {
        OrderCreatedEvent event = orderEvent();
        when(paymentRepository.findByOrderId(11L))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(savedPayment()));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            payment.setPaymentId("payment-11");
            return payment;
        });

        paymentService.createPayment(event);
        paymentService.createPayment(event);

        verify(paymentRepository).save(any(Payment.class));
        assertEquals(PaymentStatus.PENDING, savedPayment().getPaymentStatus());
    }

    // Test payment completion and event publishing.
    @Test
    void completingPaymentSetsPaidAtAndPublishesEvent() {
        Payment payment = savedPayment();
        when(paymentRepository.findById("payment-11")).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentStatusUpdateRequest request = new PaymentStatusUpdateRequest();
        request.setStatus(PaymentStatus.COMPLETED);

        assertEquals(PaymentStatus.COMPLETED,
                paymentService.updatePaymentStatus("payment-11", request).getPaymentStatus());
        assertNotNull(payment.getPaidAt());
        verify(paymentEventProducer).publish(any(PaymentCompletedEvent.class));
    }

    // Test that a completed payment is not published again.
    @Test
    void completingAnAlreadyCompletedPaymentDoesNotPublishAgain() {
        Payment payment = savedPayment();
        payment.completePayment();
        when(paymentRepository.findById("payment-11")).thenReturn(Optional.of(payment));

        PaymentStatusUpdateRequest request = new PaymentStatusUpdateRequest();
        request.setStatus(PaymentStatus.COMPLETED);

        paymentService.updatePaymentStatus("payment-11", request);

        verify(paymentEventProducer, never()).publish(any());
    }

    // Test finding a payment by order ID.
    @Test
    void paymentCanBeFoundByOrderId() {
        when(paymentRepository.findByOrderId(11L)).thenReturn(Optional.of(savedPayment()));

        assertEquals(11L, paymentService.getPaymentByOrderId(11L).getOrderId());
    }

    // Test the payment list query.
    @Test
    void existingPaymentListEndpointUsesRepository() {
        when(paymentRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(savedPayment()));

        assertEquals(1, paymentService.getAllPayments().size());
    }

    // Build a sample order event.
    private OrderCreatedEvent orderEvent() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(11L);
        event.setCustomerId(1L);
        event.setTotalAmount(new BigDecimal("20.00"));
        event.setPaymentMethod("CASH");
        event.setDeliveryAddress("Penang");
        return event;
    }

    // Build a sample payment.
    private Payment savedPayment() {
        Payment payment = new Payment(11L, 1L, new BigDecimal("20.00"), "CASH", "Penang");
        payment.setPaymentId("payment-11");
        return payment;
    }
}

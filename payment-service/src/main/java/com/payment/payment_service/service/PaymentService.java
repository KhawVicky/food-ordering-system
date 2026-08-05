package com.payment.payment_service.service;

import com.payment.payment_service.dto.OrderCreatedEvent;
import com.payment.payment_service.dto.PaymentResponse;
import com.payment.payment_service.dto.PaymentStatusUpdateRequest;
import com.payment.payment_service.exception.DuplicatePaymentException;
import com.payment.payment_service.exception.InvalidStatusTransitionException;
import com.payment.payment_service.exception.PaymentNotFoundException;
import com.payment.payment_service.messaging.event.PaymentCompletedEvent;
import com.payment.payment_service.messaging.producer.PaymentEventProducer;
import com.payment.payment_service.model.Payment;
import com.payment.payment_service.model.PaymentStatus;
import com.payment.payment_service.repository.PaymentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentService(
            PaymentRepository paymentRepository,
            PaymentEventProducer paymentEventProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
    }

    /**
     * Kept for compatibility with the original manual testing endpoint.
     * Normal payment creation is driven by OrderCreatedEvent consumption.
     */
    @Transactional
    public PaymentResponse createPayment(OrderCreatedEvent event) {
        validateOrderEvent(event);

        Payment existingPayment = paymentRepository.findByOrderId(event.getOrderId()).orElse(null);
        if (existingPayment != null) {
            return PaymentResponse.fromEntity(existingPayment);
        }

        BigDecimal amount = event.getEffectiveTotalAmount();
        Payment payment = new Payment(
                event.getOrderId(),
                event.getCustomerId(),
                amount,
                event.getPaymentMethod().trim(),
                event.getDeliveryAddress()
        );

        try {
            return PaymentResponse.fromEntity(paymentRepository.save(payment));
        } catch (DataIntegrityViolationException exception) {
            return paymentRepository.findByOrderId(event.getOrderId())
                    .map(PaymentResponse::fromEntity)
                    .orElseThrow(() -> new DuplicatePaymentException(
                            "A payment already exists for order: " + event.getOrderId()));
        }
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PaymentResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(String paymentId) {
        return PaymentResponse.fromEntity(findPayment(paymentId));
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        return PaymentResponse.fromEntity(paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(
                        "Payment not found for order: " + orderId)));
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(
            String paymentId,
            PaymentStatusUpdateRequest request) {
        Payment payment = findPayment(paymentId);
        PaymentStatus requestedStatus = request.getPaymentStatus();

        if (requestedStatus == null) {
            throw new IllegalArgumentException("Payment status is required");
        }

        PaymentStatus currentStatus = payment.getPaymentStatus();
        if (currentStatus == requestedStatus) {
            return PaymentResponse.fromEntity(payment);
        }

        if (currentStatus == PaymentStatus.COMPLETED || currentStatus == PaymentStatus.FAILED) {
            throw new InvalidStatusTransitionException(
                    "Cannot change payment status from " + currentStatus + " to " + requestedStatus);
        }

        if (requestedStatus == PaymentStatus.COMPLETED) {
            payment.completePayment();
            Payment savedPayment = paymentRepository.save(payment);
            paymentEventProducer.publish(toPaymentCompletedEvent(savedPayment));
            return PaymentResponse.fromEntity(savedPayment);
        }

        if (requestedStatus == PaymentStatus.FAILED) {
            payment.failPayment();
            return PaymentResponse.fromEntity(paymentRepository.save(payment));
        }

        throw new InvalidStatusTransitionException(
                "Cannot change payment status from " + currentStatus + " to " + requestedStatus);
    }

    private Payment findPayment(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + paymentId));
    }

    private void validateOrderEvent(OrderCreatedEvent event) {
        if (event == null || event.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID is required");
        }
        if (event.getEffectiveTotalAmount() == null
                || event.getEffectiveTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }
        if (event.getPaymentMethod() == null || event.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException("Payment method is required");
        }
    }

    private PaymentCompletedEvent toPaymentCompletedEvent(Payment payment) {
        return new PaymentCompletedEvent(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getCustomerId(),
                payment.getAmount(),
                payment.getPaymentStatus().name(),
                payment.getDeliveryAddress(),
                payment.getPaidAt()
        );
    }
}

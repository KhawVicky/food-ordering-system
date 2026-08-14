package com.payment.payment_service.controller;

import com.payment.payment_service.dto.OrderCreatedEvent;
import com.payment.payment_service.dto.PaymentResponse;
import com.payment.payment_service.dto.PaymentStatusUpdateRequest;
import com.payment.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    // Create the payment controller.
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Create a payment from an order event.
    @PostMapping("/create")
    public PaymentResponse createPayment(@Valid @RequestBody OrderCreatedEvent event) {
        return paymentService.createPayment(event);
    }

    // Get all payments.
    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // Get one payment by ID.
    @GetMapping("/{paymentId}")
    public PaymentResponse getPaymentById(@PathVariable String paymentId) {
        return paymentService.getPaymentById(paymentId);
    }

    // Get a payment by order ID.
    @GetMapping("/order/{orderId}")
    public PaymentResponse getPaymentByOrderId(@PathVariable Long orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }

    // Change the payment status.
    @PutMapping("/{paymentId}/status")
    public PaymentResponse updatePaymentStatus(
            @PathVariable String paymentId,
            @Valid @RequestBody PaymentStatusUpdateRequest request) {
        return paymentService.updatePaymentStatus(paymentId, request);
    }
}

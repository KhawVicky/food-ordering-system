package com.payment.payment_service.messaging.consumer;

import com.payment.payment_service.dto.OrderCreatedEvent;
import com.payment.payment_service.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventConsumer {

    private final PaymentService paymentService;

    // Create the order event consumer.
    public OrderCreatedEventConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Receive a new order event.
    @KafkaListener(
            topics = "order-created-topic",
            groupId = "payment-service-order-group"
    )
    public void consume(OrderCreatedEvent event) {
        paymentService.createPayment(event);
    }
}

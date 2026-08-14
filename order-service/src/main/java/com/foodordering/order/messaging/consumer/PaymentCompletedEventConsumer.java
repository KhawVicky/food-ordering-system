package com.foodordering.order.messaging.consumer;

import com.foodordering.order.messaging.event.PaymentCompletedEvent;
import com.foodordering.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedEventConsumer {

    private final OrderService orderService;

    // Create the payment event consumer.
    public PaymentCompletedEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    // Receive a completed payment event.
    @KafkaListener(
            topics = "payment-completed-topic",
            groupId = "order-service-payment-group"
    )
    public void consume(PaymentCompletedEvent event) {
        orderService.handlePaymentCompleted(event);
    }
}

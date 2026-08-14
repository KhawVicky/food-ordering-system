package com.foodordering.delivery.messaging.consumer;

import com.foodordering.delivery.messaging.event.PaymentCompletedEvent;
import com.foodordering.delivery.service.DeliveryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedEventConsumer {

    private final DeliveryService deliveryService;

    // Create the payment event consumer.
    public PaymentCompletedEventConsumer(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    // Receive a completed payment event.
    @KafkaListener(
            topics = "payment-completed-topic",
            groupId = "delivery-service-payment-group"
    )
    public void consume(PaymentCompletedEvent event) {
        deliveryService.createDelivery(event);
    }
}

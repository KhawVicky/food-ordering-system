package com.payment.payment_service.messaging.producer;

import com.payment.payment_service.messaging.config.KafkaTopicConfig;
import com.payment.payment_service.messaging.event.PaymentCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(PaymentCompletedEvent event) {
        kafkaTemplate.send(
                KafkaTopicConfig.PAYMENT_COMPLETED_TOPIC,
                String.valueOf(event.getOrderId()),
                event
        );
    }
}

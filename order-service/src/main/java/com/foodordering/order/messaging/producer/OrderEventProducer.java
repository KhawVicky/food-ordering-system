package com.foodordering.order.messaging.producer;

import com.foodordering.order.messaging.config.KafkaTopicConfig;
import com.foodordering.order.messaging.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderCreatedEvent event) {
        kafkaTemplate.send(
                KafkaTopicConfig.ORDER_CREATED_TOPIC,
                String.valueOf(event.getOrderId()),
                event
        );
    }
}

package com.payment.payment_service.messaging.event;

/**
 * Kafka-facing type kept in the messaging package while retaining the
 * original DTO as the compatible manual creation contract.
 */
public class OrderCreatedEvent extends com.payment.payment_service.dto.OrderCreatedEvent {

    public OrderCreatedEvent() {
        super();
    }
}

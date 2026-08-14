package com.foodordering.order.service;

import com.foodordering.order.dto.OrderCreateRequest;
import com.foodordering.order.dto.OrderResponse;
import com.foodordering.order.dto.OrderStatusUpdateRequest;
import com.foodordering.order.exception.InvalidStatusTransitionException;
import com.foodordering.order.exception.OrderNotFoundException;
import com.foodordering.order.messaging.event.OrderCreatedEvent;
import com.foodordering.order.messaging.event.PaymentCompletedEvent;
import com.foodordering.order.messaging.producer.OrderEventProducer;
import com.foodordering.order.model.Order;
import com.foodordering.order.model.OrderStatus;
import com.foodordering.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    // Create the order service.
    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    // Save an order and publish its event.
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        Order order = new Order(
                request.getCustomerId(),
                request.getCustomerName().trim(),
                request.getCustomerEmail().trim(),
                request.getFoodItem().trim(),
                request.getQuantity(),
                request.getTotalAmount(),
                request.getPaymentMethod().trim(),
                request.getDeliveryAddress().trim()
        );

        Order savedOrder = orderRepository.save(order);
        orderEventProducer.publish(toOrderCreatedEvent(savedOrder));
        return OrderResponse.fromEntity(savedOrder);
    }

    // Get all orders from newest to oldest.
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(OrderResponse::fromEntity)
                .toList();
    }

    // Get one order by ID.
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        return OrderResponse.fromEntity(findOrder(orderId));
    }

    // Get orders for one customer.
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(OrderResponse::fromEntity)
                .toList();
    }

    // Update an order status.
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = findOrder(orderId);
        OrderStatus requestedStatus = request.getStatus();

        if (requestedStatus == order.getOrderStatus()) {
            return OrderResponse.fromEntity(order);
        }

        if (requestedStatus == OrderStatus.PAID && order.getOrderStatus() == OrderStatus.PENDING_PAYMENT) {
            order.markAsPaid();
        } else if (requestedStatus == OrderStatus.CANCELLED
                && order.getOrderStatus() == OrderStatus.PENDING_PAYMENT) {
            order.cancel();
        } else {
            throw new InvalidStatusTransitionException(
                    "Cannot change order status from " + order.getOrderStatus() + " to " + requestedStatus
            );
        }

        return OrderResponse.fromEntity(orderRepository.save(order));
    }

    // Mark an order as paid after payment is complete.
    @Transactional
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        if (event == null || !event.isCompleted() || event.getOrderId() == null) {
            return;
        }

        Order order = findOrder(event.getOrderId());
        if (order.getOrderStatus() != OrderStatus.PAID) {
            order.markAsPaid();
            orderRepository.save(order);
        }
    }

    // Find an order or report an error.
    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
    }

    // Build the Kafka event for an order.
    private OrderCreatedEvent toOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(
                order.getOrderId(),
                order.getCustomerId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getDeliveryAddress(),
                order.getCreatedAt()
        );
    }
}

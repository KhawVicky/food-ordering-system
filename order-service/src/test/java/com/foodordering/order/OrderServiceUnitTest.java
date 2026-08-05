package com.foodordering.order;

import com.foodordering.order.dto.LoginRequest;
import com.foodordering.order.dto.OrderCreateRequest;
import com.foodordering.order.exception.InvalidLoginException;
import com.foodordering.order.messaging.event.PaymentCompletedEvent;
import com.foodordering.order.messaging.producer.OrderEventProducer;
import com.foodordering.order.model.Order;
import com.foodordering.order.model.OrderStatus;
import com.foodordering.order.model.User;
import com.foodordering.order.model.UserRole;
import com.foodordering.order.repository.OrderRepository;
import com.foodordering.order.repository.UserRepository;
import com.foodordering.order.service.AuthService;
import com.foodordering.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventProducer orderEventProducer;

    private AuthService authService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository);
        orderService = new OrderService(orderRepository, orderEventProducer);
    }

    @Test
    void successfulLoginReturnsCustomerDetails() {
        User user = new User("Demo Customer", "customer@test.com", "customer123", UserRole.CUSTOMER);
        user.setUserId(1L);
        when(userRepository.findByEmailIgnoreCase("customer@test.com")).thenReturn(Optional.of(user));

        assertEquals(UserRole.CUSTOMER,
                authService.login(new LoginRequest("customer@test.com", "customer123")).getRole());
    }

    @Test
    void failedLoginIsRejected() {
        when(userRepository.findByEmailIgnoreCase("customer@test.com")).thenReturn(Optional.empty());

        assertThrows(InvalidLoginException.class,
                () -> authService.login(new LoginRequest("customer@test.com", "wrong")));
    }

    @Test
    void createOrderStartsPendingAndPublishesEvent() {
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(7L);
            return order;
        });

        OrderCreateRequest request = new OrderCreateRequest();
        request.setCustomerId(1L);
        request.setCustomerName("Demo Customer");
        request.setCustomerEmail("customer@test.com");
        request.setFoodItem("Chicken Rice");
        request.setQuantity(2);
        request.setTotalAmount(new BigDecimal("20.00"));
        request.setPaymentMethod("CASH");
        request.setDeliveryAddress("12 Jalan Example, Penang");

        assertEquals(OrderStatus.PENDING_PAYMENT, orderService.createOrder(request).getOrderStatus());
        verify(orderEventProducer).publish(any());
    }

    @Test
    void customerOrdersAreFilteredByCustomerId() {
        Order order = new Order(1L, "Demo Customer", "customer@test.com", "Noodles", 1,
                new BigDecimal("8.00"), "CASH", "Penang");
        when(orderRepository.findByCustomerIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(order));

        assertEquals(1, orderService.getOrdersByCustomer(1L).size());
        verify(orderRepository).findByCustomerIdOrderByCreatedAtDesc(1L);
    }

    @Test
    void paymentCompletedEventMarksOrderPaidAndDuplicateIsSafe() {
        Order order = new Order(1L, "Demo Customer", "customer@test.com", "Noodles", 1,
                new BigDecimal("8.00"), "CASH", "Penang");
        when(orderRepository.findById(9L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentCompletedEvent event = new PaymentCompletedEvent(
                "payment-1", 9L, 1L, new BigDecimal("8.00"), "COMPLETED", "Penang", null);
        orderService.handlePaymentCompleted(event);
        orderService.handlePaymentCompleted(event);

        assertEquals(OrderStatus.PAID, order.getOrderStatus());
        verify(orderRepository).save(order);
        verify(orderEventProducer, never()).publish(any());
    }
}

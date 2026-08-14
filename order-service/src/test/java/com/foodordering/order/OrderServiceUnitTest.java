package com.foodordering.order;

import com.foodordering.order.dto.LoginRequest;
import com.foodordering.order.dto.OrderCreateRequest;
import com.foodordering.order.dto.RegisterRequest;
import com.foodordering.order.exception.DuplicateEmailException;
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

    // Create fresh services before each test.
    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository);
        orderService = new OrderService(orderRepository, orderEventProducer);
    }

    // Test a successful customer login.
    @Test
    void successfulLoginReturnsCustomerDetails() {
        User user = new User("Demo Customer", "customer@test.com", "customer123", UserRole.CUSTOMER);
        user.setUserId(1L);
        when(userRepository.findByEmailIgnoreCase("customer@test.com")).thenReturn(Optional.of(user));

        assertEquals(UserRole.CUSTOMER,
                authService.login(new LoginRequest("customer@test.com", "customer123")).getRole());
    }

    // Test that a bad login is rejected.
    @Test
    void failedLoginIsRejected() {
        when(userRepository.findByEmailIgnoreCase("customer@test.com")).thenReturn(Optional.empty());

        assertThrows(InvalidLoginException.class,
                () -> authService.login(new LoginRequest("customer@test.com", "wrong")));
    }

    // Test customer registration and email cleanup.
    @Test
    void registrationCreatesCustomerWithNormalizedEmail() {
        when(userRepository.existsByEmailIgnoreCase("new.customer@example.com")).thenReturn(false);
        when(userRepository.saveAndFlush(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserId(12L);
            return user;
        });

        var response = authService.register(new RegisterRequest(
                " New Customer ", "New.Customer@Example.com", "password123"));

        assertEquals(12L, response.getUserId());
        assertEquals("New Customer", response.getName());
        assertEquals("new.customer@example.com", response.getEmail());
        assertEquals(UserRole.CUSTOMER, response.getRole());
        verify(userRepository).saveAndFlush(any(User.class));
    }

    // Test that a used email is rejected.
    @Test
    void registrationRejectsExistingEmail() {
        when(userRepository.existsByEmailIgnoreCase("customer@test.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class,
                () -> authService.register(new RegisterRequest(
                        "Another Customer", "CUSTOMER@test.com", "password123")));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    // Test order creation and event publishing.
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

    // Test customer order filtering.
    @Test
    void customerOrdersAreFilteredByCustomerId() {
        Order order = new Order(1L, "Demo Customer", "customer@test.com", "Noodles", 1,
                new BigDecimal("8.00"), "CASH", "Penang");
        when(orderRepository.findByCustomerIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(order));

        assertEquals(1, orderService.getOrdersByCustomer(1L).size());
        verify(orderRepository).findByCustomerIdOrderByCreatedAtDesc(1L);
    }

    // Test payment events and duplicate safety.
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

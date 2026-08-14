package com.foodordering.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrderCreateRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Customer email must be valid")
    private String customerEmail;

    @NotBlank(message = "Food item is required")
    private String foodItem;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    // Create an empty order request.
    public OrderCreateRequest() {
    }

    // Get the customer ID.
    public Long getCustomerId() {
        return customerId;
    }

    // Set the customer ID.
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // Get the customer name.
    public String getCustomerName() {
        return customerName;
    }

    // Set the customer name.
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Get the customer email.
    public String getCustomerEmail() {
        return customerEmail;
    }

    // Set the customer email.
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    // Get the food item.
    public String getFoodItem() {
        return foodItem;
    }

    // Set the food item.
    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    // Get the quantity.
    public Integer getQuantity() {
        return quantity;
    }

    // Set the quantity.
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Get the total amount.
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    // Set the total amount.
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Get the payment method.
    public String getPaymentMethod() {
        return paymentMethod;
    }

    // Set the payment method.
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Get the delivery address.
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Set the delivery address.
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}

package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Order;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderResponseDTO {
    private Long id;
    private Order.Status status;
    private Order.OrderType type;
    private String comments;
    private Set<FoodItemOrderDTO> foodItemOrders;  // Another DTO for this
    // getters, setters, and other fields...
    private Long customerId;
    private Long employeeId;

    private Integer customer_count;
    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.type = order.getType();
        this.comments = order.getComments();
        this.customer_count = order.getCustomer_count();
        //  this.foodItemOrders = order.getFoodItemOrders();
        if (order.getFoodItemOrders() != null) {
            this.foodItemOrders = order.getFoodItemOrders().stream()
                    .map(FoodItemOrderDTO::new)  // Assuming FoodItemOrderDTO also has a similar constructor
                    .collect(Collectors.toSet());
            // Set customer and employee IDs from the relationships
            this.customerId = (order.getCustomer() != null) ? order.getCustomer().getId() : null;
            this.employeeId = (order.getAssignedEmployee() != null) ? order.getAssignedEmployee().getId() : null;
        }
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Order.Status getStatus() {
        return status;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }

    public Order.OrderType getType() {
        return type;
    }

    public void setType(Order.OrderType type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getCustomer_count() {
        return customer_count;
    }

    public void setCustomer_count(Integer customer_count) {
        this.customer_count = customer_count;
    }

    public Set<FoodItemOrderDTO> getFoodItemOrders() {
        return foodItemOrders;
    }

    public void setFoodItemOrders(Set<FoodItemOrderDTO> foodItemOrders) {
        this.foodItemOrders = foodItemOrders;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}


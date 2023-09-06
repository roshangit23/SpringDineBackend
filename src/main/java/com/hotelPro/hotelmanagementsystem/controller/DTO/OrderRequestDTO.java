package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class OrderRequestDTO {
   // private String status;
    @NotBlank
    private String type;
    private String comments;
    private Set<FoodItemOrderDTO> foodItemOrders;
    private Long customerId;
    private Long employeeId;
    private Integer customer_count;

    // Constructors, getters and setters

    public OrderRequestDTO() {}

    public OrderRequestDTO(String type, String comments, Integer customer_count, Set<FoodItemOrderDTO> foodItemOrders, Long customerId, Long employeeId) {
       // this.status = status;
        this.type = type;
        this.comments = comments;
        this.customer_count = customer_count;
        this.foodItemOrders = foodItemOrders;
        this.customerId = customerId;
        this.employeeId = employeeId;
    }

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Integer getCustomer_count() {
        return customer_count;
    }

    public void setCustomer_count(Integer customer_count) {
        this.customer_count = customer_count;
    }
}

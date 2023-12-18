package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.model.OrderAudit;

import java.time.LocalDateTime;

public class OrderAuditResponseDTO {

    private Long id;
    private Long orderNo;
    private Order.Status status;
    private Order.OrderType type;
    private String comments;
    private Integer customerCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime deletedAt;
    private String orderComments;
    private Long restaurantSectionId;
    private Long restaurantTableId;
    private String foodItemOrdersSummary;
    private Long customerId;
    private Long employeeId;

    // Constructors
    public OrderAuditResponseDTO() {
    }

    public OrderAuditResponseDTO(OrderAudit orderAudit) {
        this.id = orderAudit.getId();
        this.orderNo = orderAudit.getOrderNo();
        this.status = orderAudit.getStatus();
        this.type = orderAudit.getType();
        this.comments = orderAudit.getComments();
        this.customerCount = orderAudit.getCustomerCount();
        this.startTime = orderAudit.getStartTime();
        this.endTime = orderAudit.getEndTime();
        this.deletedAt = orderAudit.getDeletedAt();
        this.orderComments = orderAudit.getOrderComments();
        this.restaurantSectionId = orderAudit.getRestaurantSectionId();
        this.restaurantTableId = orderAudit.getRestaurantTableId();
        this.foodItemOrdersSummary = orderAudit.getFoodItemOrdersSummary();
        this.customerId = orderAudit.getCustomerId();
        this.employeeId = orderAudit.getEmployeeId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
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

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getOrderComments() {
        return orderComments;
    }

    public void setOrderComments(String orderComments) {
        this.orderComments = orderComments;
    }

    public Long getRestaurantSectionId() {
        return restaurantSectionId;
    }

    public void setRestaurantSectionId(Long restaurantSectionId) {
        this.restaurantSectionId = restaurantSectionId;
    }

    public Long getRestaurantTableId() {
        return restaurantTableId;
    }

    public void setRestaurantTableId(Long restaurantTableId) {
        this.restaurantTableId = restaurantTableId;
    }

    public String getFoodItemOrdersSummary() {
        return foodItemOrdersSummary;
    }

    public void setFoodItemOrdersSummary(String foodItemOrdersSummary) {
        this.foodItemOrdersSummary = foodItemOrdersSummary;
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

    @Override
    public String toString() {
        return "OrderAuditResponseDTO{" +
                "id=" + id +
                ", orderNo=" + orderNo +
                ", status=" + status +
                ", type=" + type +
                ", comments='" + comments + '\'' +
                ", customerCount=" + customerCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", deletedAt=" + deletedAt +
                ", orderComments='" + orderComments + '\'' +
                ", restaurantSectionId=" + restaurantSectionId +
                ", restaurantTableId=" + restaurantTableId +
                ", foodItemOrdersSummary='" + foodItemOrdersSummary + '\'' +
                ", customerId=" + customerId +
                ", employeeId=" + employeeId +
                '}';
    }
}

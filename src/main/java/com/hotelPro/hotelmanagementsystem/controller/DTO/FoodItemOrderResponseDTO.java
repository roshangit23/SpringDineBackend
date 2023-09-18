package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;

import java.time.LocalDateTime;

public class FoodItemOrderResponseDTO {
    private Long id;
    private Long foodItemId;
    private Integer quantity;
    private String comments;
    private FoodItemOrder.Status status;
    private LocalDateTime placeTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long orderId;

    public FoodItemOrderResponseDTO(FoodItemOrder fio) {
        this.id = fio.getId();
        this.foodItemId = fio.getFoodItem().getId();
        this.quantity = fio.getQuantity();
        this.comments = fio.getComments();
        this.status = fio.getStatus();
        this.placeTime = fio.getPlace_time();
        this.startTime = fio.getStart_time();
        this.endTime = fio.getEnd_time();
        this.orderId = fio.getOrder().getId();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public FoodItemOrder.Status getStatus() {
        return status;
    }

    public void setStatus(FoodItemOrder.Status status) {
        this.status = status;
    }

    public LocalDateTime getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(LocalDateTime placeTime) {
        this.placeTime = placeTime;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}

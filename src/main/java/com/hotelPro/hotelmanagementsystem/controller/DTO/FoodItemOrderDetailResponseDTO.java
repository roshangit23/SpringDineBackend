package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import com.hotelPro.hotelmanagementsystem.model.FoodItemOrderDetail;

import java.time.LocalDateTime;

public class FoodItemOrderDetailResponseDTO {
    private Long id;
    private Long kotNo;
    private Long foodItemId;
    private Long foodItemOrderId;
    private Integer quantity;
    private FoodItemOrder.Status status;

    private FoodItemOrder.Status FoodItemOrderStatus;
    private LocalDateTime placeTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public FoodItemOrderDetailResponseDTO(FoodItemOrderDetail foodItemOrderDetail) {
        this.id = foodItemOrderDetail.getId();
        this.kotNo = foodItemOrderDetail.getKotNo();
        this.foodItemId = foodItemOrderDetail.getFoodItemOrder().getFoodItem().getId();
        this.foodItemOrderId = foodItemOrderDetail.getFoodItemOrder().getId();
        this.quantity = foodItemOrderDetail.getQuantity();
        this.status = foodItemOrderDetail.getStatus();
        this.FoodItemOrderStatus = foodItemOrderDetail.getFoodItemOrder().getStatus();
        this.placeTime = foodItemOrderDetail.getPlace_time();
        this.startTime = foodItemOrderDetail.getStart_time();
        this.endTime = foodItemOrderDetail.getEnd_time();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKotNo() {
        return kotNo;
    }

    public void setKotNo(Long kotNo) {
        this.kotNo = kotNo;
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public Long getFoodItemOrderId() {
        return foodItemOrderId;
    }

    public void setFoodItemOrderId(Long foodItemOrderId) {
        this.foodItemOrderId = foodItemOrderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public FoodItemOrder.Status getStatus() {
        return status;
    }

    public void setStatus(FoodItemOrder.Status status) {
        this.status = status;
    }

    public FoodItemOrder.Status getFoodItemOrderStatus() {
        return FoodItemOrderStatus;
    }

    public void setFoodItemOrderStatus(FoodItemOrder.Status foodItemOrderStatus) {
        FoodItemOrderStatus = foodItemOrderStatus;
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
}

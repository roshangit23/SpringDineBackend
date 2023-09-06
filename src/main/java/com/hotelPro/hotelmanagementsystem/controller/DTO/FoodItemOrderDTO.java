package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import jakarta.validation.constraints.NotNull;

public class FoodItemOrderDTO {
    private Long id;
    @NotNull
    private Long foodItemId;
    @NotNull
    private Integer quantity;
    private String comments;

    // constructors, getters, setters...

    public FoodItemOrderDTO() {}

    public FoodItemOrderDTO(FoodItemOrder foodItemOrder) {
        this.id = foodItemOrder.getId();
        this.foodItemId = foodItemOrder.getFoodItem().getId();
        this.quantity = foodItemOrder.getQuantity();
        this.comments = foodItemOrder.getComments();
    }

    // getters and setters...

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
}

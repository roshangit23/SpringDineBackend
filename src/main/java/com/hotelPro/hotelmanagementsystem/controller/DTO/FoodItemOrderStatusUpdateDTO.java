package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import jakarta.validation.constraints.NotNull;

public class FoodItemOrderStatusUpdateDTO {

    @NotNull(message = "Status is required")
    private FoodItemOrder.Status status;

    public FoodItemOrderStatusUpdateDTO() {
    }

    // getters and setters

    public FoodItemOrder.Status getStatus() {
        return status;
    }

    public void setStatus(FoodItemOrder.Status status) {
        this.status = status;
    }
}

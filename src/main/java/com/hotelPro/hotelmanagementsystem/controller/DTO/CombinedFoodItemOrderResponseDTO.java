package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;

import java.util.List;
import java.util.stream.Collectors;

public class CombinedFoodItemOrderResponseDTO {
    private FoodItemOrderResponseDTO foodItemOrder;
    private List<FoodItemOrderDetailResponseDTO> foodItemOrderDetails;

    public CombinedFoodItemOrderResponseDTO(FoodItemOrder foodItemOrder) {
        this.foodItemOrder = new FoodItemOrderResponseDTO(foodItemOrder);
        this.foodItemOrderDetails = foodItemOrder.getFoodItemOrderDetails().stream()
                .map(FoodItemOrderDetailResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Getters and setters

    public FoodItemOrderResponseDTO getFoodItemOrder() {
        return foodItemOrder;
    }

    public void setFoodItemOrder(FoodItemOrderResponseDTO foodItemOrder) {
        this.foodItemOrder = foodItemOrder;
    }

    public List<FoodItemOrderDetailResponseDTO> getFoodItemOrderDetails() {
        return foodItemOrderDetails;
    }

    public void setFoodItemOrderDetails(List<FoodItemOrderDetailResponseDTO> foodItemOrderDetails) {
        this.foodItemOrderDetails = foodItemOrderDetails;
    }
}

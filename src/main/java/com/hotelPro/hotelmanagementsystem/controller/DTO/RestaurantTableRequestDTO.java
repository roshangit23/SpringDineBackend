package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RestaurantTableRequestDTO {
    @NotBlank
    private String category;
    @NotNull
    private Integer tableNumber;

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }
}


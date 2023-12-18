package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.RestaurantTable;

public class RestaurantTableResponseDTO {

    private Long id;
    private RestaurantTable.TableStatus status;
    private String category;
    private int tableNumber;
    private Long currentOrderId;

    // Constructor to transform Entity to DTO
    public RestaurantTableResponseDTO(RestaurantTable restaurantTable) {
        this.id = restaurantTable.getId();
        this.status = restaurantTable.getStatus();
        this.category = restaurantTable.getCategory();
        this.tableNumber = restaurantTable.getTableNumber();
        this.currentOrderId = restaurantTable.getCurrentOrderId();
    }

    // Getters

    public Long getId() {
        return id;
    }

    public RestaurantTable.TableStatus getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public Long getCurrentOrderId() {
        return currentOrderId;
    }
}

package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.RestaurantTable;

public class TableStatusDTO {
    private String status;

    public TableStatusDTO(RestaurantTable.TableStatus status) {
        this.status = status.name();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

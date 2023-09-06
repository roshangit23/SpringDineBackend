package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Order;

import java.time.LocalDateTime;

public class TableOrderResponseDTO {
    private Long id;
    private String status;
    private LocalDateTime startTime;

    public TableOrderResponseDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus().toString();
        this.startTime = order.getStartTime();
    }

    // getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}

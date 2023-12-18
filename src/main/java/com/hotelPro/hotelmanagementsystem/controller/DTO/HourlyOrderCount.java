package com.hotelPro.hotelmanagementsystem.controller.DTO;

public class HourlyOrderCount {
    private Integer hour;
    private Long orderCount;

    // getters and setters...

    public HourlyOrderCount() {
    }

    public HourlyOrderCount(Integer hour, Long orderCount) {
        this.hour = hour;
        this.orderCount = orderCount;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
}

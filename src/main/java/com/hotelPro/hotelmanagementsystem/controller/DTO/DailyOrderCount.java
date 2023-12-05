package com.hotelPro.hotelmanagementsystem.controller.DTO;

public class DailyOrderCount {
    private Integer day;
    private Long orderCount;

    // getters and setters...
    public DailyOrderCount() {
    }
    public Integer getDay() {
        return day;
    }
    public void setDay(Integer day) {
        this.day = day;
    }
    public Long getOrderCount() {
        return orderCount;
    }
    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
}

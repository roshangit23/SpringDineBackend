package com.hotelPro.hotelmanagementsystem.controller.DTO;

public class TopCustomerDTO {
    private Long customerId;
    private String customerName;
    private Long orderCount;

    public TopCustomerDTO() {
    }

    public TopCustomerDTO(Long customerId, String customerName, Long orderCount) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.orderCount = orderCount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
}

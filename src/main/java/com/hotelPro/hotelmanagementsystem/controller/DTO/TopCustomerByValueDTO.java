package com.hotelPro.hotelmanagementsystem.controller.DTO;

public class TopCustomerByValueDTO {
    private Long customerId;
    private String customerName;
    private Double totalBillValue;

    public TopCustomerByValueDTO() {
    }
    // Getters and setters

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

    public Double getTotalBillValue() {
        return totalBillValue;
    }

    public void setTotalBillValue(Double totalBillValue) {
        this.totalBillValue = totalBillValue;
    }
}

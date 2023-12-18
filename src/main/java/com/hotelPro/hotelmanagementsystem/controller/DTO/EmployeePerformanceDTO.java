package com.hotelPro.hotelmanagementsystem.controller.DTO;

public class EmployeePerformanceDTO {
    private Long employeeId;
    private String employeeName; // Add other necessary fields
    private Long orderCount;


    public EmployeePerformanceDTO(Long employeeId, String employeeName, Long orderCount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.orderCount = orderCount;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
// Getters and setters
}

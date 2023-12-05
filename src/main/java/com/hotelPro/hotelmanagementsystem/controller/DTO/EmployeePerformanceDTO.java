package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Employee;

public class EmployeePerformanceDTO {

    private Employee employee;
    private Long orderCount;

    // ...constructors, getters, and setters

    public EmployeePerformanceDTO() {
    }

    public EmployeePerformanceDTO(Employee employee, Long orderCount) {
        this.employee = employee;
        this.orderCount = orderCount;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
}

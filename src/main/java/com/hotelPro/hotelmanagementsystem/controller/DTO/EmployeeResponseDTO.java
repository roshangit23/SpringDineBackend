package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Employee;

public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private double salary;

    private String phoneNumber;

    // Default constructor
    public EmployeeResponseDTO() {
    }

    // Constructor to populate the DTO from an Employee object
    public EmployeeResponseDTO(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.position = employee.getPosition();
        this.salary = employee.getSalary();
        this.phoneNumber = employee.getPhoneNumber();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}


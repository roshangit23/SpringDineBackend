package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Customer;

public class CustomerResponseDTO {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    // Default constructor
    public CustomerResponseDTO() {
    }

    // Constructor to populate the DTO from a Customer object
    public CustomerResponseDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.phoneNumber = customer.getPhoneNumber();
        this.email = customer.getEmail();
        this.address = customer.getAddress();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

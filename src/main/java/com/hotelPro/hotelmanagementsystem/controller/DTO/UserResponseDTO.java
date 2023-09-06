package com.hotelPro.hotelmanagementsystem.controller.DTO;

import java.util.Set;

public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String mobileNumber;
    private Set<String> roles;

    // Constructors
    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String username, String email, String mobileNumber, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.roles = roles;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

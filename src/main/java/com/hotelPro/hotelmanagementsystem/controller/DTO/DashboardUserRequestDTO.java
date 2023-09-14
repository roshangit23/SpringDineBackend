package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.*;

import java.util.List;

public class DashboardUserRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, message = "Password should be at least 8 characters.")
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number format.")
    private String mobileNumber;
    @NotEmpty
    private List<Long> companyIds;

    // Constructors, getters, and setters...

    public DashboardUserRequestDTO(String username, String password, String email, String mobileNumber, List<Long> companyIds) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.companyIds = companyIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Long> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(List<Long> companyIds) {
        this.companyIds = companyIds;
    }
}

package com.hotelPro.hotelmanagementsystem.controller.DTO;

import java.util.List;

public class JwtResponse {

    private String token;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private String mobileNumber;
    private List<String> roles;
    private String planName;
    private String companyName;
    public JwtResponse(String token, String refreshToken, Long id, String username, String email, String mobileNumber,List<String> roles,String companyName,String planName) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.roles = roles;
        this.companyName = companyName;
        this.planName = planName;
    }

    // Getters, setters, and other standard methods

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

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

    public List<String> getRoles() {
        return roles;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

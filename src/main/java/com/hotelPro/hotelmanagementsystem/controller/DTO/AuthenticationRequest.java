package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.NotBlank;

public class AuthenticationRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    // Constructors
    public AuthenticationRequest() {}

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
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

}

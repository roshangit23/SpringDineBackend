package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.NotNull;

public class TokenRefreshRequest {
    @NotNull
    private String refreshToken;

    // Default constructor
    public TokenRefreshRequest() {
    }

    // Parameterized constructor
    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getter
    public String getRefreshToken() {
        return refreshToken;
    }

    // Setter
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}


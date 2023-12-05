package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.NotNull;

public class DeleteBillRequest {
    @NotNull
    private String comments;

    public DeleteBillRequest() {
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

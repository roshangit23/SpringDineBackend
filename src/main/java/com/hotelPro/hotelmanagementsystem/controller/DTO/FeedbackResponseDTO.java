package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Feedback;

public class FeedbackResponseDTO {

    private Long id;
    private String content;
    private String companyName;

    // Constructor mapping entity to dto
    public FeedbackResponseDTO(Feedback feedback) {
        this.id = feedback.getId();
        this.content = feedback.getContent();
        this.companyName = feedback.getCompany().getName();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCompanyName() {
        return companyName;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

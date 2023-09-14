package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.NotNull;

public class AddRemoveCompanyDTO {
    @NotNull
    private Long companyId;

    // Getters, setters, constructors...
    public AddRemoveCompanyDTO() {
        // Default constructor
    }

    public AddRemoveCompanyDTO(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
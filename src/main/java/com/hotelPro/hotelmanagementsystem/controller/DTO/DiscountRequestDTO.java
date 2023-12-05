package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class DiscountRequestDTO {

    @NotBlank
    private String discountCode;

    @NotNull
    private Double percentage;

    private Set<String> applicableOrderTypes;  // Updated to Set<String>

    private Double minimumBillAmount;

    private Set<String> applicablePaymentModes;  // Updated to Set<String>

    // Getters and Setters

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Set<String> getApplicableOrderTypes() {  // Updated return type
        return applicableOrderTypes;
    }

    public void setApplicableOrderTypes(Set<String> applicableOrderTypes) {  // Updated parameter type
        this.applicableOrderTypes = applicableOrderTypes;
    }

    public Double getMinimumBillAmount() {
        return minimumBillAmount;
    }

    public void setMinimumBillAmount(Double minimumBillAmount) {
        this.minimumBillAmount = minimumBillAmount;
    }

    public Set<String> getApplicablePaymentModes() {  // Updated return type
        return applicablePaymentModes;
    }

    public void setApplicablePaymentModes(Set<String> applicablePaymentModes) {  // Updated parameter type
        this.applicablePaymentModes = applicablePaymentModes;
    }
}

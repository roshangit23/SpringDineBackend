package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DiscountRequestDTO {
    @NotBlank
    private String discountCode;
    @NotNull
    private Double percentage;
    private String applicableOrderType; // Accepting as String
    private Double minimumBillAmount;
    private String applicablePaymentMode; // Accepting as String

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

    public String getApplicableOrderType() {
        return applicableOrderType;
    }

    public void setApplicableOrderType(String applicableOrderType) {
        this.applicableOrderType = applicableOrderType;
    }

    public Double getMinimumBillAmount() {
        return minimumBillAmount;
    }

    public void setMinimumBillAmount(Double minimumBillAmount) {
        this.minimumBillAmount = minimumBillAmount;
    }

    public String getApplicablePaymentMode() {
        return applicablePaymentMode;
    }

    public void setApplicablePaymentMode(String applicablePaymentMode) {
        this.applicablePaymentMode = applicablePaymentMode;
    }
}

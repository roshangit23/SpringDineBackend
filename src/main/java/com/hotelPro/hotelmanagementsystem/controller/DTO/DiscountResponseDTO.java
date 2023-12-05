package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Discount;

import java.util.Set;
import java.util.stream.Collectors;

public class DiscountResponseDTO {

    private Long id;
    private String discountCode;
    private Double percentage;
    private Set<String> applicableOrderTypes;  // Updated to Set<String>
    private Double minimumBillAmount;
    private Set<String> applicablePaymentModes;  // Updated to Set<String>

    public DiscountResponseDTO(Discount discount) {
        this.id = discount.getId();
        this.discountCode = discount.getDiscountCode();
        this.percentage = discount.getPercentage();

        // Convert the Enum sets to String sets for the response
        if(discount.getApplicableOrderType() != null) {
            this.applicableOrderTypes = discount.getApplicableOrderType().stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet());
        } else {
            this.applicableOrderTypes = Set.of();  // Assign an empty set if null
        }
        this.minimumBillAmount = discount.getMinimumBillAmount();

        if(discount.getApplicablePaymentMode() != null) {
            this.applicablePaymentModes = discount.getApplicablePaymentMode().stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet());
        } else {
            this.applicablePaymentModes = Set.of();  // Assign an empty set if null
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getMinimumBillAmount() {
        return minimumBillAmount;
    }

    public void setMinimumBillAmount(Double minimumBillAmount) {
        this.minimumBillAmount = minimumBillAmount;
    }

    public Set<String> getApplicableOrderTypes() {
        return applicableOrderTypes;
    }

    public void setApplicableOrderTypes(Set<String> applicableOrderTypes) {
        this.applicableOrderTypes = applicableOrderTypes;
    }

    public Set<String> getApplicablePaymentModes() {
        return applicablePaymentModes;
    }

    public void setApplicablePaymentModes(Set<String> applicablePaymentModes) {
        this.applicablePaymentModes = applicablePaymentModes;
    }

// Other methods if needed
}

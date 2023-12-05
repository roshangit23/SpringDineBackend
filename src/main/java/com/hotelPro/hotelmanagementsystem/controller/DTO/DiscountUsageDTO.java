package com.hotelPro.hotelmanagementsystem.controller.DTO;

public class DiscountUsageDTO {

    private String discountCode;
    private Long usageCount;

    // ... constructor, getters, and setters ...

    public DiscountUsageDTO(String discountCode,Long usageCount) {
        this.discountCode = discountCode;
        this.usageCount = usageCount;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }
}


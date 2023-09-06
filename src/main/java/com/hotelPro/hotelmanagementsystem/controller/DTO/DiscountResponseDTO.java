package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.Order;

public class DiscountResponseDTO {

    private Long id;
    private String discountCode;
    private Double percentage;
    private Order.OrderType applicableOrderType;
    private Double minimumBillAmount;
    private Bill.PaymentMode applicablePaymentMode;
    // If you want to include specific customer details, you can include a set of CustomerDTO here
    // private Set<CustomerResponseDTO> applicableCustomers;

    public DiscountResponseDTO(Discount discount) {
        this.id = discount.getId();
        this.discountCode = discount.getDiscountCode();
        this.percentage = discount.getPercentage();
        this.applicableOrderType = discount.getApplicableOrderType();;
        this.minimumBillAmount = discount.getMinimumBillAmount();
        this.applicablePaymentMode = discount.getApplicablePaymentMode();;
        // Convert the applicableCustomers set to DTO if needed
        // this.applicableCustomers = discount.getApplicableCustomers().stream().map(CustomerResponseDTO::new).collect(Collectors.toSet());
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

    public Order.OrderType getApplicableOrderType() {
        return applicableOrderType;
    }

    public void setApplicableOrderType(Order.OrderType applicableOrderType) {
        this.applicableOrderType = applicableOrderType;
    }

    public Double getMinimumBillAmount() {
        return minimumBillAmount;
    }

    public void setMinimumBillAmount(Double minimumBillAmount) {
        this.minimumBillAmount = minimumBillAmount;
    }

    public Bill.PaymentMode getApplicablePaymentMode() {
        return applicablePaymentMode;
    }

    public void setApplicablePaymentMode(Bill.PaymentMode applicablePaymentMode) {
        this.applicablePaymentMode = applicablePaymentMode;
    }

    // Other methods if needed
}

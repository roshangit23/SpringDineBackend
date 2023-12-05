package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.RestaurantSection;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class BillResponseDTO {

    private Long id;
    private Long billNo;
    private Long orderId;
    private double amount;
    private double cgst;
    private double sgst;
    private double totalAmount;
    private Double dueAmount;
    private Bill.BillStatus status;
    private Set<Bill.PaymentMode> paymentMode;
    private Long tableId;
    private LocalDateTime billCreatedTime;
    private Long customerId;
    private String customerName; // Assuming the Customer class has a name attribute
    private Long discountId;
    private Set<RestaurantSection.RestaurantType> restaurantType;
    // Constructor
    public BillResponseDTO(Bill bill) {
        this.id = bill.getId();
        this.billNo = bill.getBillNo();
        this.orderId = bill.getOrder().getId();
        this.amount = bill.getAmount();
        this.cgst = bill.getCgst();
        this.sgst = bill.getSgst();
        this.totalAmount = bill.getTotalAmount();
        this.status = bill.getStatus();
        this.paymentMode = bill.getPaymentMode();
        if (bill.getRestaurantTable() != null) {
            this.tableId = bill.getRestaurantTable().getId();
        } else {
            this.tableId = null;
        }
        this.billCreatedTime = bill.getBillCreatedTime();

        if(bill.getCustomer() != null) {
            this.customerId = bill.getCustomer().getId();
        }
        if(bill.getDiscount() != null) {
            this.discountId = bill.getDiscount().getId();
        }
        if(bill.getDueAmount()!=null){
            this.dueAmount = bill.getDueAmount();
        }
        this.restaurantType = bill.getCompany().getRestaurantSections().stream()
                .map(RestaurantSection::getRestaurantType)
                .collect(Collectors.toSet());
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBillNo() {
        return billNo;
    }

    public void setBillNo(Long billNo) {
        this.billNo = billNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCgst() {
        return cgst;
    }
    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Bill.BillStatus getStatus() {
        return status;
    }

    public void setStatus(Bill.BillStatus status) {
        this.status = status;
    }

    public Set<Bill.PaymentMode> getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Set<Bill.PaymentMode> paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public LocalDateTime getBillCreatedTime() {
        return billCreatedTime;
    }

    public void setBillCreatedTime(LocalDateTime billCreatedTime) {
        this.billCreatedTime = billCreatedTime;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Set<RestaurantSection.RestaurantType> getRestaurantTypes() {
        return restaurantType;
    }

    public void setRestaurantTypes(Set<RestaurantSection.RestaurantType> restaurantTypes) {
        this.restaurantType = restaurantTypes;
    }
}


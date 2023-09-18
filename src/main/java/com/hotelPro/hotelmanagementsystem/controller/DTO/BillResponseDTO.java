package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Bill;

import java.time.LocalDateTime;
import java.util.Set;

public class BillResponseDTO {

    private Long id;
    private Long orderId;
    private double amount;
    private Double dueAmount;
    private Bill.BillStatus status;
    private Set<Bill.PaymentMode> paymentMode;
    private Long tableId;
    private LocalDateTime billCreatedTime;
    private Long customerId;
    private String customerName; // Assuming the Customer class has a name attribute

    // Constructor
    public BillResponseDTO(Bill bill) {
        this.id = bill.getId();
        this.orderId = bill.getOrder().getId();
        this.amount = bill.getAmount();
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

        if(bill.getDueAmount()!=null){
            this.dueAmount = bill.getDueAmount();
        }
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

}


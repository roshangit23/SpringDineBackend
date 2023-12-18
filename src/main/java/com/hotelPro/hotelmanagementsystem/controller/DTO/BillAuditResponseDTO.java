package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.BillAudit;

import java.time.LocalDateTime;
import java.util.Set;

public class BillAuditResponseDTO {

    private Long id;
    private Long billNo;
    private double amount;
    private double cgst;
    private double sgst;
    private double totalAmount;
    private Double dueAmount;
    private Bill.BillStatus status;
    private Set<Bill.PaymentMode> paymentMode;
    private Long orderId;
    private Long restaurantTableId;
    private Long customerId;
    private Long discountId;
    private LocalDateTime billCreatedTime;
    private LocalDateTime deletedAt;
    private String comments;

    // Constructors
    public BillAuditResponseDTO() {
    }

    public BillAuditResponseDTO(BillAudit billAudit) {
        this.id = billAudit.getId();
        this.billNo = billAudit.getBillNo();
        this.amount = billAudit.getAmount();
        this.cgst = billAudit.getCgst();
        this.sgst = billAudit.getSgst();
        this.totalAmount = billAudit.getTotalAmount();
        this.dueAmount = billAudit.getDueAmount();
        this.status = billAudit.getStatus();
        this.paymentMode = billAudit.getPaymentMode();
        this.orderId = billAudit.getOrderId();
        this.restaurantTableId = billAudit.getRestaurantTableId();
        this.customerId = billAudit.getCustomerId();
        this.discountId = billAudit.getDiscountId();
        this.billCreatedTime = billAudit.getBillCreatedTime();
        this.deletedAt = billAudit.getDeletedAt();
        this.comments = billAudit.getComments();
    }

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getRestaurantTableId() {
        return restaurantTableId;
    }

    public void setRestaurantTableId(Long restaurantTableId) {
        this.restaurantTableId = restaurantTableId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public LocalDateTime getBillCreatedTime() {
        return billCreatedTime;
    }

    public void setBillCreatedTime(LocalDateTime billCreatedTime) {
        this.billCreatedTime = billCreatedTime;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "BillAuditResponseDTO{" +
                "id=" + id +
                ", billNo=" + billNo +
                ", amount=" + amount +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", totalAmount=" + totalAmount +
                ", dueAmount=" + dueAmount +
                ", status=" + status +
                ", paymentMode=" + paymentMode +
                ", orderId=" + orderId +
                ", restaurantTableId=" + restaurantTableId +
                ", customerId=" + customerId +
                ", discountId=" + discountId +
                ", billCreatedTime=" + billCreatedTime +
                ", deletedAt=" + deletedAt +
                ", comments='" + comments + '\'' +
                '}';
    }
}

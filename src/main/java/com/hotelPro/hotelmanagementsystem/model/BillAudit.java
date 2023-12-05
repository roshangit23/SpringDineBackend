package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bill_audit",uniqueConstraints = @UniqueConstraint(columnNames = {"bill_no", "company_id"}))
public class BillAudit implements CompanyAssociatedEntity {
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public enum BillStatus {
        SETTLED, NOT_SETTLED
    }
    public enum PaymentMode {
        CASH, CARD, DUE, OTHER, PART
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_no")
    private Long billNo;

    @Column(name = "amount")
    private double amount;

    @Column(name = "cgst")
    private double cgst;

    @Column(name = "sgst")
    private double sgst;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "due_amount")
    private Double dueAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Bill.BillStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private Set<Bill.PaymentMode> paymentMode;

    @Column(name = "bill_created_time", updatable = false)
    private LocalDateTime billCreatedTime;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "comments")
    private String comments;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    // Constructors, getters, setters, etc.

    public BillAudit() {
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
}

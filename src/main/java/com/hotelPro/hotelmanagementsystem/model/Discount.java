package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name = "discount",
        uniqueConstraints = @UniqueConstraint(columnNames = {"discount_code", "company_id"}))
public class Discount implements CompanyAssociatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "discount_code", nullable = false)
    private String discountCode;
    @NotNull
    private Double percentage;

    @Enumerated(EnumType.STRING)
    private Order.OrderType applicableOrderType; // Specific order type to which discount is applicable

    private Double minimumBillAmount; // Minimum bill amount for discount applicability

    @Enumerated(EnumType.STRING)
    private Bill.PaymentMode applicablePaymentMode; // Specific payment mode to which discount is applicable

    @ManyToMany
    @JoinTable(name = "discount_customers", joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private Set<Customer> applicableCustomers; // Specific customers to which discount is applicable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    public Discount() {
        // default constructor
    }

    public Discount(String discountCode, double percentage, Order.OrderType applicableOrderType, double minimumBillAmount, Bill.PaymentMode applicablePaymentMode, Set<Customer> applicableCustomers) {
        this.discountCode = discountCode;
        this.percentage = percentage;
        this.applicableOrderType = applicableOrderType;
        this.minimumBillAmount = minimumBillAmount;
        this.applicablePaymentMode = applicablePaymentMode;
        this.applicableCustomers = applicableCustomers;
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
        this.applicableOrderType =applicableOrderType;
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

    public Set<Customer> getApplicableCustomers() {
        return applicableCustomers;
    }

    public void setApplicableCustomers(Set<Customer> applicableCustomers) {
        this.applicableCustomers = applicableCustomers;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(name = "applicable_order_type", length = 255)
    private String applicableOrderTypeString;

    @Column(name = "applicable_payment_mode", length = 255)
    private String applicablePaymentModeString;
    private Double minimumBillAmount; // Minimum bill amount for discount applicability

    @ManyToMany
    @JoinTable(name = "discount_customers", joinColumns = @JoinColumn(name = "discount_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private Set<Customer> applicableCustomers; // Specific customers to which discount is applicable
    @OneToMany(mappedBy = "discount", fetch = FetchType.LAZY)
    private Set<Bill> bills;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    public Discount() {
        // default constructor
    }

    public Discount(String discountCode, double percentage, Set<Order.OrderType> applicableOrderTypes, Double minimumBillAmount, Set<Bill.PaymentMode> applicablePaymentModes, Set<Customer> applicableCustomers) {
        this.discountCode = discountCode;
        this.percentage = percentage;
        setApplicableOrderType(applicableOrderTypes);
        this.minimumBillAmount = minimumBillAmount;
        setApplicablePaymentMode(applicablePaymentModes);
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

    public Set<Order.OrderType> getApplicableOrderType() {
        if (this.applicableOrderTypeString == null || this.applicableOrderTypeString.trim().isEmpty()) {
            return new HashSet<>();  // return an empty set if the string is null or empty
        }
        return Arrays.stream(this.applicableOrderTypeString.split(","))
                .map(String::trim)
                .map(Order.OrderType::valueOf)
                .collect(Collectors.toSet());
    }

    public void setApplicableOrderType(Set<Order.OrderType> applicableOrderType) {
        this.applicableOrderTypeString = applicableOrderType.stream()
                .map(Order.OrderType::name)
                .collect(Collectors.joining(","));
    }

    public Set<Bill.PaymentMode> getApplicablePaymentMode() {
        if (this.applicablePaymentModeString == null || this.applicablePaymentModeString.trim().isEmpty()) {
            return new HashSet<>();  // return an empty set if the string is null or empty
        }
        return new HashSet<>(Arrays.asList(applicablePaymentModeString.split(","))
                .stream()
                .map(String::trim)
                .map(Bill.PaymentMode::valueOf)
                .collect(Collectors.toSet()));
    }

    public void setApplicablePaymentMode(Set<Bill.PaymentMode> applicablePaymentMode) {
        this.applicablePaymentModeString = applicablePaymentMode.stream()
                .map(Bill.PaymentMode::name)
                .collect(Collectors.joining(","));
    }

    public Double getMinimumBillAmount() {
        return minimumBillAmount;
    }

    public void setMinimumBillAmount(Double minimumBillAmount) {
        this.minimumBillAmount = minimumBillAmount;
    }


    public Set<Customer> getApplicableCustomers() {
        return applicableCustomers;
    }

    public void setApplicableCustomers(Set<Customer> applicableCustomers) {
        this.applicableCustomers = applicableCustomers;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
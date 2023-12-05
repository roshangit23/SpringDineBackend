package com.hotelPro.hotelmanagementsystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    public enum SubscriptionName {
        DESKTOP,
        DESKTOP_CAP_KIT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionName name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;
    @Column
    private LocalDate expiryDate;
    @OneToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public Subscription() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionName getName() {
        return name;
    }

    public void setName(SubscriptionName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

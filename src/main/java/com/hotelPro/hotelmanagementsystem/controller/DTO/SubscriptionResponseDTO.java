package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Subscription;

import java.time.LocalDate;

public class SubscriptionResponseDTO {
    private Long id;
    private Subscription.SubscriptionName name;
    private String description;
    private Double price;
    private LocalDate expiryDate;

    // Constructor to convert Subscription entity to DTO
    public SubscriptionResponseDTO(Subscription subscription) {
        this.id = subscription.getId();
        this.name = subscription.getName();
        this.description = subscription.getDescription();
        this.price = subscription.getPrice();
        // Assuming Subscription entity has a getExpiryDate method
        this.expiryDate = subscription.getExpiryDate();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subscription.SubscriptionName getName() {
        return name;
    }

    public void setName(Subscription.SubscriptionName name) {
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
}
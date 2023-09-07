package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "inventory",
        uniqueConstraints = @UniqueConstraint(columnNames = {"item_name","company_id"}))
public class Inventory implements CompanyAssociatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "item_name",unique = true)
    private String itemName;
    @NotNull
    @Min(0)
    @Column(name = "quantity")
    private Integer quantity;
    @Min(0)
    @Column(name = "smart_quantity")
    private Integer smartQuantity;
    @NotNull
    @Positive
    @Column(name = "price_per_unit")
    private Double pricePerUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    // Constructors
    public Inventory() {
        // default constructor
    }

    public Inventory(String itemName, int quantity, double pricePerUnit) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSmartQuantity() {
        return smartQuantity;
    }

    public void setSmartQuantity(Integer smartQuantity) {
        this.smartQuantity = smartQuantity;
    }
    @PrePersist
    public void setDefaultSmartQuantity() {
        if (this.smartQuantity == null) {
            this.smartQuantity = 0;
        }
    }
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

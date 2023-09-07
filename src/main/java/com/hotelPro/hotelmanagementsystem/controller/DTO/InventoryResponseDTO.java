package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Inventory;

public class InventoryResponseDTO {

    private Long id;
    private String itemName;
    private int quantity;
    private double pricePerUnit;
    private int smartQuantity;

    // Default constructor
    public InventoryResponseDTO() {
    }

    // Constructor to populate the DTO from an Inventory object
    public InventoryResponseDTO(Inventory inventory) {
        this.id = inventory.getId();
        this.itemName = inventory.getItemName();
        this.quantity = inventory.getQuantity();
        this.pricePerUnit = inventory.getPricePerUnit();
        this.smartQuantity = inventory.getSmartQuantity();
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getSmartQuantity() {
        return smartQuantity;
    }

    public void setSmartQuantity(int smartQuantity) {
        this.smartQuantity = smartQuantity;
    }
}

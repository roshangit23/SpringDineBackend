package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Inventory;

public class InventoryResponseDTO {

    private Long id;
    private String itemName;
    private int quantity;
    private double pricePerUnit;

    // Default constructor
    public InventoryResponseDTO() {
    }

    // Constructor to populate the DTO from an Inventory object
    public InventoryResponseDTO(Inventory inventory) {
        this.id = inventory.getId();
        this.itemName = inventory.getItemName();
        this.quantity = inventory.getQuantity();
        this.pricePerUnit = inventory.getPricePerUnit();
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
}

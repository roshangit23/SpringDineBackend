package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Inventory;

public class IngredientUsageDTO {

    private Inventory inventory;
    private Integer usage;

    // ...constructors, getters, and setters

    public IngredientUsageDTO(Inventory inventory, Integer usage) {
        this.inventory = inventory;
        this.usage = usage;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }
}
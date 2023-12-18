package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Inventory;

public class IngredientUsageDTO {

    private long inventoryId;
    private Integer usage;

    // ...constructors, getters, and setters

    public IngredientUsageDTO(Inventory inventory, Integer usage) {
        inventoryId = inventory.getId();
        this.usage = usage;
    }


    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }
}
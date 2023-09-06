package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.FoodItem;
public class FoodItemResponseDTO {
    private Long id;

    private String itemName;

    private Double itemPrice;

    private String category;

    private String type;
    private String description;

    // constructors, getters, setters...
    public FoodItemResponseDTO() {
    }

    public FoodItemResponseDTO(FoodItem foodItem) {
        this.id = foodItem.getId();
        this.itemName = foodItem.getItemName();
        this.itemPrice = foodItem.getItemPrice();
        this.category = foodItem.getCategory();
        this.type = foodItem.getType();
        this.description = foodItem.getDescription();
    }

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

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

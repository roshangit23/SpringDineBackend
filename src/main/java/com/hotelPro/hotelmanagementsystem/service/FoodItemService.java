package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.FoodItem;

import java.util.List;

public interface FoodItemService {
    FoodItem saveFoodItem(FoodItem foodItem, Long companyId);
    FoodItem getFoodItemById(Long id);
    List<FoodItem> getAllFoodItems(Long companyId);
    void deleteFoodItem(Long id);

    FoodItem findById(Long id);

    //FoodItem findByItemName(String itemName);
    FoodItem findByItemNameAndCompanyId(String itemName, Long companyId);

    FoodItem updateFoodItem(Long id, FoodItem foodItem);


    FoodItem findByShortCodeAndCompanyId(String shortCode, Long companyId);
}

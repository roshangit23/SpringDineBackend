package com.hotelPro.hotelmanagementsystem.service.operations;

import com.hotelPro.hotelmanagementsystem.model.FoodItem;
import com.hotelPro.hotelmanagementsystem.service.FoodItemService;
import org.springframework.stereotype.Service;
@Service
public class FoodItemOps {
    private final FoodItemService foodItemService;

    public FoodItemOps(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }
    // 1) Add food item
    public void createFoodItem() {
        FoodItem foodItem = new FoodItem("Appa", 15.00,"Starter","Veg","fast food");
        System.out.println("Saving foodItem: " + foodItem.getItemName());
        //foodItemService.saveFoodItem(foodItem);
        System.out.println("Done!");
    }

    // 2) Get all food items
    public void getAllFoodItems() {
        //List<FoodItem> foodItems = foodItemService.getAllFoodItems();
       // System.out.println("All foodItems: " + foodItems);
        System.out.println("Done!");
    }

    // 3) Get food item by name
    public void getFoodItemByName(String itemName) {
        FoodItem foodItem = foodItemService.findByItemName(itemName);
        System.out.println("FoodItem by name: " + foodItem);
        System.out.println("Done!");
    }

    // 4) Get food item by id
    public void getFoodItemById(Long id) {
        FoodItem foodItem = foodItemService.findById(id);
        System.out.println("FoodItem by id: " + foodItem);
        System.out.println("Done!");
    }

    // 5) Update food item by id
    public void updateFoodItemById(Long id, String newFoodItemName) {
        FoodItem foodItem = foodItemService.findById(id);
        foodItem.setItemName(newFoodItemName);
        //foodItemService.saveFoodItem(foodItem);
        System.out.println("Updated foodItem: " + foodItem);
        System.out.println("Done!");
    }

    // 6) Delete food item
    public void deleteFoodItem(Long id) {
        foodItemService.deleteFoodItem(id);
        System.out.println("Deleted foodItem with id: " + id);
        System.out.println("Done!");
    }
}

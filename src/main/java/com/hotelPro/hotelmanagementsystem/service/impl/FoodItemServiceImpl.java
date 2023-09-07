package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.FoodItem;
import com.hotelPro.hotelmanagementsystem.model.FoodItemInventory;
import com.hotelPro.hotelmanagementsystem.model.Inventory;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.FoodItemRepository;
import com.hotelPro.hotelmanagementsystem.repository.InventoryRepository;
import com.hotelPro.hotelmanagementsystem.service.FoodItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemServiceImpl implements FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Override
    @Transactional
    public FoodItem saveFoodItem(FoodItem foodItem, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        foodItem.setCompany(company);

        // Validate the required inventory items
        List<FoodItemInventory> foodItemInventories = foodItem.getRequiredInventoryItems();

        // Check if foodItemInventories is not null before processing
        if (foodItemInventories != null) {
            for (FoodItemInventory foodItemInventory : foodItemInventories) {
                Long inventoryId = foodItemInventory.getInventory().getId();
                Integer requiredQuantity = foodItemInventory.getRequiredQuantity();

                // Validate the inventory item's ID
                Inventory validatedInventory = inventoryRepository.findById(inventoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", inventoryId));

                // Set the validated inventory to the foodItemInventory
                foodItemInventory.setInventory(validatedInventory);
                foodItemInventory.setRequiredQuantity(requiredQuantity); // This might be redundant if it's already set, but just to be sure
                foodItemInventory.setFoodItem(foodItem); // Ensure the relationship is set
            }
        }

        return foodItemRepository.save(foodItem);
    }


    @Override
    @Transactional
    public FoodItem getFoodItemById(Long id) {
        //Optional<FoodItem> optionalFoodItem = foodItemRepository.findById(id);
        //return optionalFoodItem.orElse(null);
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "id", id));
        return foodItem;

    }

    @Override
    @Transactional
    public List<FoodItem> getAllFoodItems(Long companyId) {
        return foodItemRepository.findByCompanyId(companyId);
        //return foodItemRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteFoodItem(Long id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "id", id));
        foodItemRepository.deleteById(id);
    }

    // in FoodItemService class
    @Override
    @Transactional
    public FoodItem findById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "id", id));
    }

    @Override
    @Transactional
    public FoodItem findByItemName(String itemName) {
        return foodItemRepository.findByItemName(itemName)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "item Name", itemName));
    }

    @Override
    public FoodItem updateFoodItem(Long id, FoodItem foodItemDetails) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "id", id));

        if (foodItemDetails.getItemName() != null && !foodItemDetails.getItemName().isEmpty()) {
            foodItem.setItemName(foodItemDetails.getItemName());
        }
        if (foodItemDetails.getDescription() != null && !foodItemDetails.getDescription().isEmpty()) {
            foodItem.setDescription(foodItemDetails.getDescription());
        }
        if (foodItemDetails.getItemPrice() != null && foodItemDetails.getItemPrice() > 0.0) { // assuming 0.0 is not a valid item price
            foodItem.setItemPrice(foodItemDetails.getItemPrice());
        }
        if (foodItemDetails.getCategory() != null && !foodItemDetails.getCategory().isEmpty()) {
            foodItem.setCategory(foodItemDetails.getCategory());
        }
        if (foodItemDetails.getType() != null && !foodItemDetails.getType().isEmpty()) {
            foodItem.setType(foodItemDetails.getType());
        }

        // Handle the required inventory items
        List<FoodItemInventory> foodItemInventories = foodItemDetails.getRequiredInventoryItems();
        if (foodItemInventories != null) {
            // Clear the old associations
            foodItem.getRequiredInventoryItems().clear();

            for (FoodItemInventory foodItemInventory : foodItemInventories) {
                Long inventoryId = foodItemInventory.getInventory().getId();
                Integer requiredQuantity = foodItemInventory.getRequiredQuantity();

                // Validate the inventory item's ID
                Inventory validatedInventory = inventoryRepository.findById(inventoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", inventoryId));

                // Set the validated inventory to the foodItemInventory
                foodItemInventory.setInventory(validatedInventory);
                foodItemInventory.setRequiredQuantity(requiredQuantity); // This might be redundant if it's already set, but just to be sure
                foodItemInventory.setFoodItem(foodItem); // Ensure the relationship is set

                // Add the foodItemInventory to the food item's list
                foodItem.getRequiredInventoryItems().add(foodItemInventory);
            }
        }

        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);
        return updatedFoodItem;
    }

}

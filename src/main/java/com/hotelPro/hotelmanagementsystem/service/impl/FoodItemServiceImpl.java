package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.exception.CustomException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        // Check if short_code1 is being used as short_code2 by any other FoodItem for the same company
        if (foodItem.getShortCode1() != null) {
            Optional<FoodItem> conflict1 = foodItemRepository.findByShortCode2AndCompanyId(foodItem.getShortCode1(), foodItem.getCompany().getId());
            if (conflict1.isPresent()) {
                throw new CustomException("The short code provided for short_code1 is already being used as short_code2 for another FoodItem in the same company.", HttpStatus.FORBIDDEN);
            }
        }

        // Check if short_code2 is being used as short_code1 by any other FoodItem for the same company
        if (foodItem.getShortCode2() != null) {
            Optional<FoodItem> conflict2 = foodItemRepository.findByShortCode1AndCompanyId(foodItem.getShortCode2(), foodItem.getCompany().getId());
            if (conflict2.isPresent()) {
                throw new CustomException("The short code provided for short_code2 is already being used as short_code1 for another FoodItem in the same company.", HttpStatus.FORBIDDEN);
            }
        }

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
                // Check if the inventory's associated companyId matches the passed companyId
                if (!validatedInventory.getCompany().getId().equals(companyId)) {
                    throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
                }
                // Set the validated inventory to the foodItemInventory
                foodItemInventory.setInventory(validatedInventory);
                foodItemInventory.setRequiredQuantity(requiredQuantity);
                foodItemInventory.setFoodItem(foodItem);
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
public FoodItem findByItemNameAndCompanyId(String itemName, Long companyId) {
    // Update the method to also search by companyId
    return foodItemRepository.findByItemNameAndCompanyId(itemName, companyId)
            .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "item Name", itemName));
}


    @Override
    @Transactional
    public FoodItem updateFoodItem(Long id, FoodItem foodItemDetails) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "id", id));

        if (foodItemDetails.getItemName() != null && !foodItemDetails.getItemName().isEmpty()) {
            foodItem.setItemName(foodItemDetails.getItemName());
        }
        if (foodItemDetails.getDescription() != null && !foodItemDetails.getDescription().isEmpty()) {
            foodItem.setDescription(foodItemDetails.getDescription());
        }
        if (foodItemDetails.getItemPrice() != null && foodItemDetails.getItemPrice() > 0.0) {
            foodItem.setItemPrice(foodItemDetails.getItemPrice());
        }
        if (foodItemDetails.getCategory() != null && !foodItemDetails.getCategory().isEmpty()) {
            foodItem.setCategory(foodItemDetails.getCategory());
        }
        if (foodItemDetails.getType() != null && !foodItemDetails.getType().isEmpty()) {
            foodItem.setType(foodItemDetails.getType());
        }
        if (foodItemDetails.getFoodType() != null) {
            foodItem.setFoodType(foodItemDetails.getFoodType());
        }
        // Updating shortCode1
        if (foodItemDetails.getShortCode1() != null && !foodItemDetails.getShortCode1().trim().isEmpty()) {
            foodItem.setShortCode1(foodItemDetails.getShortCode1());
        }

        // Updating shortCode2
        if (foodItemDetails.getShortCode2() != null && !foodItemDetails.getShortCode2().trim().isEmpty()) {
            foodItem.setShortCode2(foodItemDetails.getShortCode2());
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
                foodItemInventory.setRequiredQuantity(requiredQuantity);
                foodItemInventory.setFoodItem(foodItem);

                // Add the foodItemInventory to the food item's list
                foodItem.getRequiredInventoryItems().add(foodItemInventory);
            }
        }

        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);
        return updatedFoodItem;
    }

    @Override
    @Transactional
    public FoodItem findByShortCodeAndCompanyId(String shortCode, Long companyId) {
        return foodItemRepository.findByShortCode1OrShortCode2AndCompanyId(shortCode, shortCode, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "short Code", shortCode));
    }
    @Override
    @Transactional
    public List<FoodItem> searchFoodItems(Long companyId, String query) {
        return foodItemRepository.findByItemNameContainingOrShortCode1ContainingOrShortCode2ContainingAndCompanyId(query, query,query, companyId);
    }

}

package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.FoodItemResponseDTO;
import com.hotelPro.hotelmanagementsystem.model.FoodItem;
import com.hotelPro.hotelmanagementsystem.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/foodItems")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @PostMapping("/createFoodItem/{companyId}")
    public ResponseEntity<ApiResponse<FoodItemResponseDTO>> saveFoodItem(@Valid @RequestBody FoodItem foodItem, @PathVariable Long companyId) {
        FoodItem createdItem = foodItemService.saveFoodItem(foodItem, companyId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), new FoodItemResponseDTO(createdItem)), HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse<FoodItemResponseDTO>> getFoodItemById(@PathVariable Long id) {
        FoodItem foodItem = foodItemService.getFoodItemById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new FoodItemResponseDTO(foodItem)));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<FoodItemResponseDTO>> getFoodItemByName(@PathVariable String name) {
        FoodItem foodItem = foodItemService.findByItemName(name);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new FoodItemResponseDTO(foodItem)));
    }

    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<FoodItemResponseDTO>>> getAllFoodItems(@PathVariable Long companyId) {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems(companyId);
        List<FoodItemResponseDTO> response = foodItems.stream()
                .map(FoodItemResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodItemResponseDTO>> updateFoodItem(@RequestBody FoodItem foodItem, @PathVariable Long id) {
        FoodItem updatedItem = foodItemService.updateFoodItem(id, foodItem);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new FoodItemResponseDTO(updatedItem)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFoodItem(@PathVariable Long id) {
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Food item deleted successfully"));
    }
}


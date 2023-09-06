package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.InventoryResponseDTO;
import com.hotelPro.hotelmanagementsystem.model.Inventory;
import com.hotelPro.hotelmanagementsystem.repository.InventoryRepository;
import com.hotelPro.hotelmanagementsystem.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping("/createInventory/{companyId}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> saveInventory(@Valid @RequestBody Inventory inventory, @PathVariable Long companyId) {
        Inventory savedInventory = inventoryService.saveInventory(inventory, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), new InventoryResponseDTO(savedInventory)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new InventoryResponseDTO(inventory)));
    }

    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<InventoryResponseDTO>>> getAllInventories(@PathVariable Long companyId) {
        List<Inventory> inventories = inventoryService.getAllInventories(companyId);
        List<InventoryResponseDTO> dtos = inventories.stream()
                .map(InventoryResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), dtos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory updatedInventory = inventoryService.updateInventory(id, inventory);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new InventoryResponseDTO(updatedInventory)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Inventory deleted successfully"));
    }
}


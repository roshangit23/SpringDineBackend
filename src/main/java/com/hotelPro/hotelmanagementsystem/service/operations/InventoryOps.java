package com.hotelPro.hotelmanagementsystem.service.operations;

import com.hotelPro.hotelmanagementsystem.model.Inventory;
import com.hotelPro.hotelmanagementsystem.service.InventoryService;
import org.springframework.stereotype.Service;
@Service
public class InventoryOps {

    private final InventoryService inventoryService;

    public InventoryOps(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void addInventory(Inventory inventory) {
        // implement the logic to add an inventory item
        // inventoryService.saveInventory(inventory);
    }

    public void getAllInventoryItems() {
        // implement the logic to get all inventory items
        // inventoryService.getAllInventories();
    }

    public void getInventoryById(Long id) {
        // implement the logic to get inventory item by id
         inventoryService.getInventoryById(id);
    }

    public void updateInventory(Long id) {
        // implement the logic to update an inventory item
        Inventory inventory = inventoryService.getInventoryById(id);
        inventory.setQuantity(25);
            // inventoryService.saveInventory(inventory);

    }

    public void deleteInventory(Long id) {
        // implement the logic to delete an inventory item
        inventoryService.deleteInventory(id);
    }

    public void updateInventory(long id,Inventory inventory){
          inventoryService.updateInventory(id, inventory);
    }
}

package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.Inventory;

import java.util.List;

public interface InventoryService {
    Inventory saveInventory(Inventory inventory, Long companyId);
    Inventory getInventoryById(Long id);
    List<Inventory> getAllInventories(Long companyId);
    void deleteInventory(Long id);
    Inventory updateInventory(Long id, Inventory inventory);
}

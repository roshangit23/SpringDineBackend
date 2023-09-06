package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.Inventory;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.InventoryRepository;
import com.hotelPro.hotelmanagementsystem.service.InventoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Override
    @Transactional
    public Inventory saveInventory(Inventory inventory, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        inventory.setCompany(company);
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
        return inventory;
    }

    @Override
    @Transactional
    public List<Inventory> getAllInventories(Long companyId) {
        return inventoryRepository.findByCompanyId(companyId);
       // return inventoryRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
        inventoryRepository.delete(inventory);
    }

    @Override
    @Transactional
    public Inventory updateInventory(Long id, Inventory inventoryDetails){
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));

        if (inventoryDetails.getItemName() != null && !inventoryDetails.getItemName().isEmpty()) {
            inventory.setItemName(inventoryDetails.getItemName());
        }
        if (inventoryDetails.getQuantity() != null && inventoryDetails.getQuantity() > 0) { // assuming 0 is not a valid quantity
            inventory.setQuantity(inventoryDetails.getQuantity());
        }
        if (inventoryDetails.getPricePerUnit() != null && inventoryDetails.getPricePerUnit() > 0.0) { // assuming 0.0 is not a valid price
            inventory.setPricePerUnit(inventoryDetails.getPricePerUnit());
        }
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return updatedInventory;
    }
}

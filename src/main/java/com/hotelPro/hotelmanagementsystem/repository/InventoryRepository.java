package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByItemName(String itemName);

    List<Inventory> findByCompanyId(Long companyId);
    // custom methods if required
}

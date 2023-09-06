package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    // custom methods if required
    Optional<FoodItem> findByItemName(String itemName);

    List<FoodItem> findByCompanyId(Long companyId);
}

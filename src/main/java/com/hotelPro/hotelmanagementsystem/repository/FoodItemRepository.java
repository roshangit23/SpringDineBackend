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

    Optional<FoodItem> findByItemNameAndCompanyId(String itemName, Long companyId);

    Optional<FoodItem> findByShortCode1OrShortCode2AndCompanyId(String shortCode1, String shortCode2, Long companyId);
    Optional<FoodItem> findByShortCode1AndCompanyId(String shortCode, Long companyId);
    Optional<FoodItem> findByShortCode2AndCompanyId(String shortCode, Long companyId);

}

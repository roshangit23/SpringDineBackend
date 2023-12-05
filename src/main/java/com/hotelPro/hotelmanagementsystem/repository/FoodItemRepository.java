package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<FoodItem> findByItemNameContainingOrShortCode1ContainingOrShortCode2ContainingAndCompanyId(String query, String query1, String query2,Long companyId);
    @Query("SELECT fi FROM FoodItem fi WHERE fi.company.id = :companyId AND (" +
            "LOWER(fi.itemName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(fi.shortCode1) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(fi.shortCode2) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<FoodItem> searchByItemNameOrShortCodes(@Param("companyId") Long companyId, @Param("query") String query);
}

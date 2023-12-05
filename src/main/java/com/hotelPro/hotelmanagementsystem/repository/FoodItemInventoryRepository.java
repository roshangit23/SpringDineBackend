package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.FoodItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface FoodItemInventoryRepository extends JpaRepository<FoodItemInventory, Long> {

    @Query("SELECT fi.inventory, SUM(fio.quantity * fi.requiredQuantity) as usage " +
            "FROM FoodItemOrder fio " +
            "JOIN fio.foodItem.requiredInventoryItems fi " +
            "WHERE fio.start_time BETWEEN :startDate AND :endDate " +
            "AND fio.company.id = :companyId " +
            "GROUP BY fi.inventory " +
            "ORDER BY usage DESC")
    List<Object[]> findMostUsedIngredients(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("companyId") Long companyId
    );

    @Query("SELECT fi.inventory, SUM(fio.quantity * fi.requiredQuantity) as usage " +
            "FROM FoodItemOrder fio " +
            "JOIN fio.foodItem.requiredInventoryItems fi " +
            "WHERE fio.start_time BETWEEN :startDate AND :endDate " +
            "AND fio.company.id = :companyId " +
            "GROUP BY fi.inventory " +
            "ORDER BY usage ASC")
    List<Object[]> findLeastUsedIngredients(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("companyId") Long companyId
    );
}

//FoodItemOrderRepository
package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FoodItemOrderRepository extends JpaRepository<FoodItemOrder, Long> {

    Set<FoodItemOrder> findByStatus(FoodItemOrder.Status status);
    Set<FoodItemOrder> findByCompanyId(Long companyId);
    Set<FoodItemOrder> findByCompanyIdAndStatus(Long companyId, FoodItemOrder.Status status);
}

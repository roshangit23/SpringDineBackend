package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import com.hotelPro.hotelmanagementsystem.model.FoodItemOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FoodItemOrderDetailRepository extends JpaRepository<FoodItemOrderDetail, Long> {
    Set<FoodItemOrderDetail> findByCompanyId(Long companyId);
    Set<FoodItemOrderDetail> findByCompanyIdAndStatus(Long companyId, FoodItemOrder.Status status);

}

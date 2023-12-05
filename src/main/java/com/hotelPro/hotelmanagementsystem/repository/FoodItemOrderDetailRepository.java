package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import com.hotelPro.hotelmanagementsystem.model.FoodItemOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface FoodItemOrderDetailRepository extends JpaRepository<FoodItemOrderDetail, Long> {
    Set<FoodItemOrderDetail> findByCompanyId(Long companyId);
    Set<FoodItemOrderDetail> findByCompanyIdAndStatus(Long companyId, FoodItemOrder.Status status);
    @Query("SELECT fod FROM FoodItemOrderDetail fod " +
            "WHERE fod.company.id = :companyId " +
            "AND fod.start_time >= :startDate " +
            "AND fod.end_time <= :endDate " +
            "AND (fod.end_time - fod.start_time) > :minDuration " +
            "AND (fod.end_time - fod.start_time) <= :maxDuration")
    List<FoodItemOrderDetail> findFoodItemOrderDetailsByTimeTaken(@Param("companyId") Long companyId,
                                                                  @Param("startDate") LocalDateTime startDate,
                                                                  @Param("endDate") LocalDateTime endDate,
                                                                  @Param("minDuration") Duration minDuration,
                                                                  @Param("maxDuration") Duration maxDuration);

    @Query("SELECT MAX(fiod.kotNo) FROM FoodItemOrderDetail fiod WHERE fiod.company.id = :companyId")
    Long findMaxKotNoByCompany(@Param("companyId") Long companyId);
}

package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query("SELECT b FROM Bill b WHERE b.order.id = :orderId")
    Optional<Bill> findByOrderId(@Param("orderId") Long orderId);
    Bill findFirstByRestaurantTableOrderByBillCreatedTimeDesc(RestaurantTable restaurantTable);
    List<Bill> findByCompanyId(Long companyId);
    @Query("SELECT MAX(b.billNo) FROM Bill b WHERE b.company.id = :companyId")
    Long findMaxBillNoByCompany(@Param("companyId") Long companyId);
    Optional<Bill> findByBillNoAndCompanyId(Long billNo, Long companyId);
}

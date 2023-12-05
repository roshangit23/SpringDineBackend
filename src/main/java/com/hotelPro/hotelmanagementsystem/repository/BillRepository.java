package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.controller.DTO.TopCustomerByValueDTO;
import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.model.RestaurantTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query("SELECT b FROM Bill b WHERE b.order.id = :orderId")
    Optional<Bill> findByOrderId(@Param("orderId") Long orderId);
    Bill findFirstByRestaurantTableOrderByBillCreatedTimeDesc(RestaurantTable restaurantTable);
    List<Bill> findByCompanyId(Long companyId);
    @Query("SELECT MAX(b.billNo) FROM Bill b WHERE b.company.id = :companyId")
    Long findMaxBillNoByCompany(@Param("companyId") Long companyId);
    Optional<Bill> findByBillNoAndCompanyId(Long billNo, Long companyId);

    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.company.id = :companyId AND b.billCreatedTime BETWEEN :startDate AND :endDate")
    Double calculateRevenue(@Param("companyId") Long companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT SUM(b.total_amount) FROM bills b WHERE b.company_id = :companyId AND b.bill_created_time BETWEEN :startDate AND :endDate AND :paymentMode = ANY (b.payment_mode)", nativeQuery = true)
    Double calculateRevenueByPaymentMode(@Param("companyId") Long companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("paymentMode") Bill.PaymentMode paymentMode);

    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.company.id = :companyId AND b.order.type = :orderType AND b.billCreatedTime BETWEEN :startDate AND :endDate")
    Double calculateRevenueByOrderType(@Param("companyId") Long companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("orderType") Order.OrderType orderType);

    @Query("SELECT SUM(b.dueAmount) FROM Bill b " +
            "WHERE b.company.id = :companyId " +
            "AND b.status = 'NOT_SETTLED' " +
            "AND b.paymentMode IN :paymentModes " +
            "AND b.billCreatedTime BETWEEN :startDate AND :endDate")
    Double calculateTotalDueAmount(
            @Param("companyId") Long companyId,
            @Param("paymentModes") Set<Bill.PaymentMode> paymentModes,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT new com.hotelPro.hotelmanagementsystem.controller.DTO.TopCustomerByValueDTO(b.customer.id, b.customer.name, SUM(b.totalAmount)) " +
            "FROM Bill b " +
            "WHERE b.company.id = :companyId " +
            "AND b.billCreatedTime BETWEEN :startDate AND :endDate " +
            "GROUP BY b.customer.id, b.customer.name " +
            "ORDER BY SUM(b.totalAmount) DESC")
    List<TopCustomerByValueDTO> findTopCustomersByBillValue(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE b.company.id = :companyId ORDER BY b.billCreatedTime DESC")
    Page<Bill> findLast10Bills(@Param("companyId") Long companyId, Pageable pageable);
}

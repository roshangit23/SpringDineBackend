package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.controller.DTO.DailyOrderCount;
import com.hotelPro.hotelmanagementsystem.controller.DTO.HourlyOrderCount;
import com.hotelPro.hotelmanagementsystem.controller.DTO.TopCustomerDTO;
import com.hotelPro.hotelmanagementsystem.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // custom methods if required
    @Query("SELECT o FROM Order o JOIN FETCH o.foodItemOrders WHERE o.id = :id")
    Optional<Order> findByIdWithFoodItemOrders(@Param("id") Long id);

    List<Order> findByCompanyId(Long companyId);
    List<Order> findByStatus(Order.Status status);

    List<Order> findByCompany_IdAndStatus(Long companyId, Order.Status status);

    List<Order> findByCompany_IdAndType(Long companyId, Order.OrderType orderType);
    @Query("SELECT MAX(o.orderNo) FROM Order o WHERE o.company.id = :companyId")
    Long findMaxOrderNoByCompany(@Param("companyId") Long companyId);

    Optional<Order> findByOrderNoAndCompanyId(Long orderNo, Long companyId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.company.id = :companyId AND o.startTime BETWEEN :startDate AND :endDate")
    Long countByCompanyIdAndDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT com.hotelPro.hotelmanagementsystem.controller.DTO.TopCustomerDTO(o.customer.id, o.customer.name, COUNT(o)) " +
            "FROM Order o " +
            "WHERE o.company.id = :companyId " +
            "AND o.startTime BETWEEN :startDate AND :endDate " +
            "GROUP BY o.customer.id, o.customer.name " +
            "ORDER BY COUNT(o) DESC")
    List<TopCustomerDTO> findTopCustomersByOrderFrequency(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.company.id = :companyId AND o.status = :status AND o.startTime BETWEEN :startDate AND :endDate")
    Long countOrdersByStatus(Long companyId, Order.Status status, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT AVG(UNIX_TIMESTAMP(o.end_time) - UNIX_TIMESTAMP(o.start_time)) FROM orders o WHERE o.company_id = :companyId AND o.status = 'COMPLETED' AND o.start_time BETWEEN :startDate AND :endDate", nativeQuery = true)
    Double findAverageOrderCompletionTime(@Param("companyId") Long companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT HOUR(o.startTime) as hour, COUNT(o.id) as orderCount FROM Order o WHERE o.company.id = :companyId AND o.startTime BETWEEN :startDate AND :endDate GROUP BY HOUR(o.startTime) ORDER BY orderCount DESC")
    List<HourlyOrderCount> findHourlyOrderCounts(Long companyId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT DAYOFWEEK(o.startTime) as day, COUNT(o.id) as orderCount FROM Order o WHERE o.company.id = :companyId AND o.startTime BETWEEN :startDate AND :endDate GROUP BY DAYOFWEEK(o.startTime) ORDER BY orderCount DESC")
    List<DailyOrderCount> findDailyOrderCounts(Long companyId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o " +
            "WHERE o.company.id = :companyId " +
            "AND o.startTime >= :startDate " +
            "AND o.endTime <= :endDate " +
            "AND (o.endTime - o.startTime) > :minDuration " +
            "AND (o.endTime - o.startTime) <= :maxDuration")
    List<Order> findOrdersByTimeTaken(@Param("companyId") Long companyId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate,
                                      @Param("minDuration") Duration minDuration,
                                      @Param("maxDuration") Duration maxDuration);
    @Query("SELECT o.type, COUNT(o) FROM Order o WHERE o.company.id = :companyId AND o.startTime BETWEEN :startDate AND :endDate GROUP BY o.type")
    List<Object[]> countOrdersByTypeAndDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    @Query("SELECT o FROM Order o WHERE o.company.id = :companyId ORDER BY o.startTime DESC")
    Page<Order> findLast10Orders(@Param("companyId") Long companyId, Pageable pageable);
    @Query("SELECT o.assignedEmployee, COUNT(o) FROM Order o WHERE o.company.id = :companyId AND o.startTime BETWEEN :startDate AND :endDate GROUP BY o.assignedEmployee")
    List<Object[]> findOrderCountByEmployee(@Param("companyId") Long companyId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

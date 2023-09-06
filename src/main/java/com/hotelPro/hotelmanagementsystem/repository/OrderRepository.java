package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}

package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findBydiscountCode(String code);
    List<Discount> findByCompanyId(Long companyId);
    @Query("SELECT d.discountCode, COUNT(b) AS usageCount " +
            "FROM Discount d JOIN d.bills b " +
            "WHERE b.company.id = :companyId " +
            "AND b.billCreatedTime BETWEEN :startDate AND :endDate " +
            "GROUP BY d.discountCode " +
            "ORDER BY COUNT(b) DESC")
    List<Object[]> findMostUsedDiscountCodes(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    Optional<Discount> findByDiscountCodeAndCompanyId(String code, Long companyId);
}

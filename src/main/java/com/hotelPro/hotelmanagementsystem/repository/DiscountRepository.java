package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findBydiscountCode(String code);

    List<Discount> findByCompanyId(Long companyId);
}

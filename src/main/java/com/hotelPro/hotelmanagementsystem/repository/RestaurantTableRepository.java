package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    // custom methods if required
    Optional<RestaurantTable> findByCategoryAndTableNumber(String category, int tableNumber);
    List<RestaurantTable> findByCurrentOrderIsNotNull();
    List<RestaurantTable> findByCompanyId(Long companyId);
    List<RestaurantTable> findByCurrentOrderIsNotNullAndCompanyId(Long companyId);
    Optional<RestaurantTable> findByTableNumberAndCategoryAndCompanyId(Integer tableNumber, String category, Long companyId);
}

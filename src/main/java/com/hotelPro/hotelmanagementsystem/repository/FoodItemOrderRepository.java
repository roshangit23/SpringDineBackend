//FoodItemOrderRepository
package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface FoodItemOrderRepository extends JpaRepository<FoodItemOrder, Long> {

    Set<FoodItemOrder> findByStatus(FoodItemOrder.Status status);
    Set<FoodItemOrder> findByCompanyId(Long companyId);
    Set<FoodItemOrder> findByCompanyIdAndStatus(Long companyId, FoodItemOrder.Status status);

    @Query("SELECT f.foodItem.itemName as itemName, SUM(f.quantity) as quantity " +
            "FROM FoodItemOrder f " +
            "WHERE f.order.company.id = :companyId AND " +
            "f.place_time BETWEEN :startDate AND :endDate " +
            "GROUP BY f.foodItem.itemName " +
            "ORDER BY SUM(f.quantity) DESC")
    List<TopSellingItemProjection> findTopSellingItems(@Param("companyId") Long companyId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f.foodItem.category as category, SUM(f.quantity) as quantity " +
            "FROM FoodItemOrder f " +
            "WHERE f.order.company.id = :companyId AND " +
            "f.place_time BETWEEN :startDate AND :endDate " +
            "GROUP BY f.foodItem.category " +
            "ORDER BY SUM(f.quantity) DESC")
    List<TopSellingCategoryProjection> findTopSellingCategories(@Param("companyId") Long companyId,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);
    @Query("SELECT f.foodItem.itemName as itemName, SUM(f.quantity) as quantity " +
            "FROM FoodItemOrder f " +
            "WHERE f.order.company.id = :companyId AND " +
            "f.place_time BETWEEN :startDate AND :endDate " +
            "GROUP BY f.foodItem.itemName " +
            "ORDER BY SUM(f.quantity) ASC")  // Note the ASC for ascending order
    List<LeastSellingItemProjection> findLeastSellingItems(@Param("companyId") Long companyId,
                                                           @Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    @Query(nativeQuery = true, value = """
        WITH item_pairs AS (
            SELECT 
                o1.food_item_id AS item_id1,
                o2.food_item_id AS item_id2,
                COUNT(*) AS frequency
            FROM
                food_item_orders o1
            JOIN
                food_item_orders o2 ON o1.order_id = o2.order_id AND o1.food_item_id < o2.food_item_id
            JOIN
                orders ord ON o1.order_id = ord.id
            WHERE
                ord.start_time BETWEEN :startDate AND :endDate
                AND ord.company_id = :companyId
            GROUP BY
                o1.food_item_id,
                o2.food_item_id
        )
        SELECT 
            f1.item_name AS item1,
            f2.item_name AS item2,
            ip.frequency
        FROM
            item_pairs ip
        JOIN
            food_items f1 ON ip.item_id1 = f1.id
        JOIN
            food_items f2 ON ip.item_id2 = f2.id
        WHERE
            f1.company_id = :companyId
            AND f2.company_id = :companyId
        ORDER BY
            ip.frequency DESC
        LIMIT 10;
    """)
    List<ItemPairFrequency> findFrequentlyOrderedItemPairs(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    public interface ItemPairFrequency {
        String getItem1();
        String getItem2();
        int getFrequency();
    }

    interface TopSellingItemProjection {
        String getItemName();
        Long getQuantity();
    }

    interface TopSellingCategoryProjection {
        String getCategory();
        Long getQuantity();
    }
    interface LeastSellingItemProjection {
        String getItemName();
        Long getQuantity();
    }

}

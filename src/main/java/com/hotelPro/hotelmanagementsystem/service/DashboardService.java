package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;
import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.repository.FoodItemOrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    Double calculateRevenue(Long companyId, String period);
    Double calculateAverageOrderValue(Long companyId, String period);
    Double calculateRevenueByPaymentMode(Long companyId, String period, Bill.PaymentMode paymentMode);
    Double calculateRevenueByOrderType(Long companyId, String period, Order.OrderType orderType);
    Double calculateTotalDueAmount(Long companyId, String period);
    List<TopCustomerDTO> getTopCustomersByOrderFrequency(Long companyId, String period);
    List<TopCustomerByValueDTO> getTopCustomersByBillValue(Long companyId, String period);
    Long countOrdersByStatus(Long companyId, Order.Status status, String period);
    Double calculateAverageOrderCompletionTime(Long companyId, String period);
    List<HourlyOrderCount> getPeakOrderTimes(Long companyId, String period);
    List<DailyOrderCount> getPeakOrderDays(Long companyId, String period);
    List<OrderAudit> getDeletedOrders(Long companyId, String period);
    List<BillAudit> getDeletedBills(Long companyId, String period);
    List<FoodItemOrderRepository.TopSellingItemProjection> getTopSellingItems(Long companyId, LocalDateTime startDate, LocalDateTime endDate);
    List<FoodItemOrderRepository.TopSellingCategoryProjection> getTopSellingCategories(Long companyId, LocalDateTime startDate, LocalDateTime endDate);
    List<FoodItemOrderRepository.LeastSellingItemProjection> getLeastSellingItems(Long companyId, LocalDateTime startDate, LocalDateTime endDate);
     List<FoodItemOrderRepository.ItemPairFrequency> getFrequentlyOrderedItemPairs(Long companyId, String period);
    List<DiscountUsageDTO> getMostUsedDiscountCodes(Long companyId, String period);
    List<Order> filterOrdersByTimeTaken(Long companyId, String timeTaken, String period);
    List<FoodItemOrderDetail> filterFoodItemOrderDetailsByTimeTaken(Long companyId, String timeTaken, String period);
    Map<Order.OrderType, Long> countOrdersByType(Long companyId, String period);
    List<Order> getLast10Orders(Long companyId);
    List<Bill> getLast10Bills(Long companyId);
    List<EmployeePerformanceDTO> getEmployeePerformance(Long companyId, String period);
    List<IngredientUsageDTO> getMostUsedIngredients(Long companyId, String period);
    List<IngredientUsageDTO> getLeastUsedIngredients(Long companyId, String period);
}

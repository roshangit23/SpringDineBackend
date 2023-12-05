package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;
import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.repository.*;
import com.hotelPro.hotelmanagementsystem.service.DashboardService;
import com.hotelPro.hotelmanagementsystem.service.DateRange;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private OrderAuditRepository orderAuditRepository;
    @Autowired
    private BillAuditRepository billAuditRepository;
    @Autowired
    private FoodItemOrderRepository foodItemOrderRepository;
    @Autowired
    private  FoodItemOrderDetailRepository foodItemOrderDetailRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private FoodItemInventoryRepository foodItemInventoryRepository;

    private DateRange getDateRange(String period) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        switch (period.toLowerCase()) {
            case "daily":
                startDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
                endDate = startDate.plusDays(1).minusNanos(1);
                break;
            case "weekly":
                startDate = LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, 1).truncatedTo(ChronoUnit.DAYS);
                endDate = startDate.plusWeeks(1).minusNanos(1);
                break;
            case "monthly":
                startDate = LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                endDate = startDate.plusMonths(1).minusNanos(1);
                break;
            case "3months":
                startDate = LocalDateTime.now().minusMonths(2).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                endDate = startDate.plusMonths(3).minusNanos(1);
                break;
            case "6months":
                startDate = LocalDateTime.now().minusMonths(5).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                endDate = startDate.plusMonths(6).minusNanos(1);
                break;
            case "yearly":
                startDate = LocalDateTime.now().withDayOfYear(1).truncatedTo(ChronoUnit.DAYS);
                endDate = startDate.plusYears(1).minusNanos(1);
                break;
            case "all-time":
                startDate = LocalDateTime.of(2000, 1, 1, 0, 0);  // Or choose an earlier date if needed
                endDate = LocalDateTime.now();
                break;
            default:
                throw new IllegalArgumentException("Invalid time period: " + period);
        }

        return new DateRange(startDate, endDate);
    }

    private Pair<Duration, Duration> getDurationRange(String timeTaken) {
        switch (timeTaken.toLowerCase()) {
            case "less than 5min":
                return Pair.of(Duration.ZERO, Duration.ofMinutes(5));
            case "less than 10min":
                return Pair.of(Duration.ZERO, Duration.ofMinutes(10));
            case "less than 15min":
                return Pair.of(Duration.ZERO, Duration.ofMinutes(15));
            case "less than 30min":
                return Pair.of(Duration.ofMinutes(15), Duration.ofMinutes(30));
            case "less than 60min":
                return Pair.of(Duration.ofMinutes(30), Duration.ofMinutes(60));
            case "more than 1hr and less than 2hr":
                return Pair.of(Duration.ofHours(1), Duration.ofHours(2));
            case "more than 2hours":
                return Pair.of(Duration.ofHours(2), Duration.ofDays(1));  // Assuming a maximum duration of 1 day
            default:
                throw new IllegalArgumentException("Invalid time taken filter: " + timeTaken);
        }
    }
    @Override
    @Transactional
    public Double calculateRevenue(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return billRepository.calculateRevenue(companyId, dateRange.getStartDate(), dateRange.getEndDate());
    }

    @Override
    @Transactional
    public Double calculateAverageOrderValue(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        Double totalRevenue = billRepository.calculateRevenue(companyId, dateRange.getStartDate(), dateRange.getEndDate());
        Long numberOfOrders = orderRepository.countByCompanyIdAndDateRange(companyId, dateRange.getStartDate(), dateRange.getEndDate());
        if (numberOfOrders == 0) {
            return 0.0;  // Avoid division by zero
        }
        return totalRevenue / numberOfOrders;
    }

    @Override
    @Transactional
    public Double calculateRevenueByPaymentMode(Long companyId, String period, Bill.PaymentMode paymentMode) {
        DateRange dateRange = getDateRange(period);
        return billRepository.calculateRevenueByPaymentMode(companyId, dateRange.getStartDate(), dateRange.getEndDate(), paymentMode);
    }

    @Override
    @Transactional
    public Double calculateRevenueByOrderType(Long companyId, String period, Order.OrderType orderType) {
        DateRange dateRange = getDateRange(period);
        return billRepository.calculateRevenueByOrderType(companyId, dateRange.getStartDate(), dateRange.getEndDate(), orderType);
    }
    @Override
    @Transactional
    public Double calculateTotalDueAmount(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);

        // Create a Set containing the DUE PaymentMode
        Set<Bill.PaymentMode> paymentModes = Collections.singleton(Bill.PaymentMode.DUE);

        // Pass the paymentModes Set to the calculateTotalDueAmount method
        return billRepository.calculateTotalDueAmount(companyId, paymentModes, dateRange.getStartDate(), dateRange.getEndDate());
    }
    @Override
    @Transactional
    public List<TopCustomerDTO> getTopCustomersByOrderFrequency(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        Pageable topCustomersPageable = PageRequest.of(0, 10);  // Limit to top 10 customers
        return orderRepository.findTopCustomersByOrderFrequency(companyId, dateRange.getStartDate(), dateRange.getEndDate(), topCustomersPageable);
    }

    @Override
    @Transactional
    public List<TopCustomerByValueDTO> getTopCustomersByBillValue(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        Pageable topCustomersPageable = PageRequest.of(0, 10);  // Limit to top 10 customers
        return billRepository.findTopCustomersByBillValue(companyId, dateRange.getStartDate(), dateRange.getEndDate(), topCustomersPageable);
    }

    @Override
    @Transactional
    public Long countOrdersByStatus(Long companyId, Order.Status status, String period) {
        DateRange dateRange = getDateRange(period);
        return orderRepository.countOrdersByStatus(companyId, status, dateRange.getStartDate(), dateRange.getEndDate());
    }
    @Override
    @Transactional
    public Double calculateAverageOrderCompletionTime(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return orderRepository.findAverageOrderCompletionTime(companyId, dateRange.getStartDate(), dateRange.getEndDate());
    }
    @Override
    @Transactional
    public List<HourlyOrderCount> getPeakOrderTimes(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return orderRepository.findHourlyOrderCounts(companyId, dateRange.getStartDate(), dateRange.getEndDate());
    }
    @Override
    @Transactional
    public List<DailyOrderCount> getPeakOrderDays(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return orderRepository.findDailyOrderCounts(companyId, dateRange.getStartDate(), dateRange.getEndDate());
    }
    @Override
    @Transactional
    public List<OrderAudit> getDeletedOrders(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return orderAuditRepository.findByDeletedAtBetweenAndCompanyId(dateRange.getStartDate(), dateRange.getEndDate(), companyId);
    }
    @Override
    @Transactional
    public List<BillAudit> getDeletedBills(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return billAuditRepository.findByDeletedAtBetweenAndCompanyId(dateRange.getStartDate(), dateRange.getEndDate(), companyId);
    }
    @Override
    @Transactional
    public List<FoodItemOrderRepository.TopSellingItemProjection> getTopSellingItems(Long companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return foodItemOrderRepository.findTopSellingItems(companyId, startDate, endDate);
    }

    @Override
    @Transactional
    public List<FoodItemOrderRepository.TopSellingCategoryProjection> getTopSellingCategories(Long companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return foodItemOrderRepository.findTopSellingCategories(companyId, startDate, endDate);
    }
    @Override
    @Transactional
    public List<FoodItemOrderRepository.LeastSellingItemProjection> getLeastSellingItems(Long companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return foodItemOrderRepository.findLeastSellingItems(companyId, startDate, endDate);
    }
    @Override
    @Transactional
    public List<FoodItemOrderRepository.ItemPairFrequency> getFrequentlyOrderedItemPairs(
            Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return foodItemOrderRepository.findFrequentlyOrderedItemPairs(
                companyId, dateRange.getStartDate(), dateRange.getEndDate()
        );
    }
    @Transactional
    public List<DiscountUsageDTO> getMostUsedDiscountCodes(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        List<Object[]> result = discountRepository.findMostUsedDiscountCodes(
                companyId, dateRange.getStartDate(), dateRange.getEndDate());
        return result.stream()
                .map(obj -> new DiscountUsageDTO((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Order> filterOrdersByTimeTaken(Long companyId, String timeTaken, String period) {
        DateRange dateRange = getDateRange(period);
        Pair<Duration, Duration> durationRange = getDurationRange(timeTaken);
        return orderRepository.findOrdersByTimeTaken(companyId,
                dateRange.getStartDate(),
                dateRange.getEndDate(),
                durationRange.getFirst(),
                durationRange.getSecond());
    }

    @Override
    @Transactional
    public List<FoodItemOrderDetail> filterFoodItemOrderDetailsByTimeTaken(Long companyId, String timeTaken, String period) {
        DateRange dateRange = getDateRange(period);
        Pair<Duration, Duration> durationRange = getDurationRange(timeTaken);
        return foodItemOrderDetailRepository.findFoodItemOrderDetailsByTimeTaken(companyId,
                dateRange.getStartDate(),
                dateRange.getEndDate(),
                durationRange.getFirst(),
                durationRange.getSecond());
    }
    @Override
    @Transactional
    public Map<Order.OrderType, Long> countOrdersByType(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        List<Object[]> results = orderRepository.countOrdersByTypeAndDateRange(companyId, dateRange.getStartDate(), dateRange.getEndDate());
        Map<Order.OrderType, Long> counts = new HashMap<>();
        for (Object[] result : results) {
            counts.put((Order.OrderType) result[0], (Long) result[1]);
        }
        return counts;
    }
    @Override
    @Transactional
    public List<Order> getLast10Orders(Long companyId) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "startTime"));
        return orderRepository.findLast10Orders(companyId, pageable).getContent();
    }
    @Override
    @Transactional
    public List<Bill> getLast10Bills(Long companyId) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "billCreatedTime"));
        return billRepository.findLast10Bills(companyId, pageable).getContent();
    }
    @Override
    @Transactional
    public List<EmployeePerformanceDTO> getEmployeePerformance(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        List<Object[]> result = orderRepository.findOrderCountByEmployee(companyId, dateRange.getStartDate(), dateRange.getEndDate());
        return result.stream()
                .map(obj -> new EmployeePerformanceDTO((Employee) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public List<IngredientUsageDTO> getMostUsedIngredients(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return mapToDTO(foodItemInventoryRepository.findMostUsedIngredients(
                dateRange.getStartDate(), dateRange.getEndDate(), companyId
        ));
    }
    @Override
    @Transactional
    public List<IngredientUsageDTO> getLeastUsedIngredients(Long companyId, String period) {
        DateRange dateRange = getDateRange(period);
        return mapToDTO(foodItemInventoryRepository.findLeastUsedIngredients(
                dateRange.getStartDate(), dateRange.getEndDate(), companyId
        ));
    }

    private List<IngredientUsageDTO> mapToDTO(List<Object[]> result) {
        return result.stream()
                .map(obj -> new IngredientUsageDTO((Inventory) obj[0], ((Number) obj[1]).intValue()))
                .collect(Collectors.toList());
    }
}

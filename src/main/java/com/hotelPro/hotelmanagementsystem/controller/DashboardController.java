package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;
import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.repository.FoodItemOrderRepository;
import com.hotelPro.hotelmanagementsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

 //Sales and Revenue Analytics:
    @GetMapping("/revenue/{companyId}")
    public ResponseEntity<ApiResponse<String>> getRevenue(  @PathVariable Long companyId, @RequestParam String period) {
        try {
            Double revenue = dashboardService.calculateRevenue(companyId, period);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Revenue calculated successfully: " + revenue));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while calculating revenue"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/aov/{companyId}")
    public ResponseEntity<ApiResponse<String>> getAverageOrderValue(@PathVariable Long companyId, @RequestParam String period) {
        Double aov = dashboardService.calculateAverageOrderValue(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Average Order Value: " + aov));
    }

    @GetMapping("/revenueByPaymentMode/{companyId}")
    public ResponseEntity<ApiResponse<String>> getRevenueByPaymentMode(@PathVariable Long companyId, @RequestParam Bill.PaymentMode paymentMode, @RequestParam String period) {
        Double revenue = dashboardService.calculateRevenueByPaymentMode(companyId, period, paymentMode);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Revenue by payment mode calculated successfully: " + revenue));
    }

    @GetMapping("/revenueByOrderType/{companyId}")
    public ResponseEntity<ApiResponse<String>> getRevenueByOrderType(
            @PathVariable Long companyId,
            @RequestParam Order.OrderType orderType,
            @RequestParam String period) {
        Double revenue = dashboardService.calculateRevenueByOrderType(companyId, period, orderType);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Revenue calculated successfully: " + revenue));
    }
    @GetMapping("/due-amount/{companyId}")
    public ResponseEntity<ApiResponse<String>> getTotalDueAmount(
            @PathVariable Long companyId,
            @RequestParam String period) {
        Double dueAmount = dashboardService.calculateTotalDueAmount(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Total due amount calculated successfully: " + dueAmount));
    }

    //Customer Analytics:
    @GetMapping("/top-customers/{companyId}")
    public ResponseEntity<ApiResponse<List<TopCustomerDTO>>> getTopCustomersByOrderFrequency(
            @PathVariable Long companyId,
            @RequestParam String period) {
        List<TopCustomerDTO> topCustomers = dashboardService.getTopCustomersByOrderFrequency(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), topCustomers));
    }
    @GetMapping("/top-customers-value/{companyId}")
    public ResponseEntity<ApiResponse<List<TopCustomerByValueDTO>>> getTopCustomersByBillValue(
            @PathVariable Long companyId,
            @RequestParam String period) {
        List<TopCustomerByValueDTO> topCustomers = dashboardService.getTopCustomersByBillValue(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), topCustomers));
    }

    @GetMapping("/order-status-count/{companyId}")
    public ResponseEntity<ApiResponse<Long>> getOrderCountByStatus(
            @PathVariable Long companyId,
            @RequestParam Order.Status status,
            @RequestParam String period) {
            Long orderCount = dashboardService.countOrdersByStatus(companyId, status, period);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), orderCount));
    }
    @GetMapping("/order-count-by-type/{companyId}")
    public ResponseEntity<ApiResponse<Map<Order.OrderType, Long>>> getOrderCountByType(
            @PathVariable Long companyId,
            @RequestParam String period
    ) {
        Map<Order.OrderType, Long> counts = dashboardService.countOrdersByType(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), counts));
    }
    @GetMapping("/average-order-completion-time/{companyId}")
    public ResponseEntity<ApiResponse<Double>> getAverageOrderCompletionTime(
            @PathVariable Long companyId,
            @RequestParam String period) {
            Double averageTime = dashboardService.calculateAverageOrderCompletionTime(companyId, period);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), averageTime));
    }
    @GetMapping("/peak-order-times/{companyId}")
    public ResponseEntity<ApiResponse<List<HourlyOrderCount>>> getPeakOrderTimes(
            @PathVariable Long companyId,
            @RequestParam String period) {
        List<HourlyOrderCount> peakTimes = dashboardService.getPeakOrderTimes(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), peakTimes));
    }
    @GetMapping("/peak-order-days/{companyId}")
    public ResponseEntity<ApiResponse<List<DailyOrderCount>>> getPeakOrderDays(
            @PathVariable Long companyId,
            @RequestParam String period) {
        List<DailyOrderCount> peakDays = dashboardService.getPeakOrderDays(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), peakDays));
    }
    @GetMapping("/deleted-orders/{companyId}")
    public ResponseEntity<ApiResponse<List<OrderAudit>>> getDeletedOrders(
            @PathVariable Long companyId,
            @RequestParam String period) {
        List<OrderAudit> deletedOrders = dashboardService.getDeletedOrders(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), deletedOrders));
    }
    @GetMapping("/deleted-bills/{companyId}")
    public ResponseEntity<ApiResponse<List<BillAudit>>> getDeletedBills(
            @PathVariable Long companyId,
            @RequestParam String period) {
        List<BillAudit> deletedBills = dashboardService.getDeletedBills(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), deletedBills));
    }

    @GetMapping("/top-selling-items/{companyId}")
    public ResponseEntity<ApiResponse<List<FoodItemOrderRepository.TopSellingItemProjection>>> getTopSellingItems(
            @PathVariable Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<FoodItemOrderRepository.TopSellingItemProjection> topSellingItems = dashboardService.getTopSellingItems(companyId, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), topSellingItems));
    }

    @GetMapping("/top-selling-categories/{companyId}")
    public ResponseEntity<ApiResponse<List<FoodItemOrderRepository.TopSellingCategoryProjection>>> getTopSellingCategories(
            @PathVariable Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<FoodItemOrderRepository.TopSellingCategoryProjection> topSellingCategories = dashboardService.getTopSellingCategories(companyId, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), topSellingCategories));
    }
    @GetMapping("/least-selling-items/{companyId}")
    public ResponseEntity<ApiResponse<List<FoodItemOrderRepository.LeastSellingItemProjection>>> getLeastSellingItems(
            @PathVariable Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<FoodItemOrderRepository.LeastSellingItemProjection> leastSellingItems = dashboardService.getLeastSellingItems(companyId, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), leastSellingItems));
    }
    @GetMapping("/frequently-ordered-items/{companyId}")
    public ResponseEntity<ApiResponse<List<FoodItemOrderRepository.ItemPairFrequency>>> getFrequentlyOrderedItemPairs(
            @PathVariable Long companyId,
            @RequestParam String period
    ) {
        List<FoodItemOrderRepository.ItemPairFrequency> frequentlyOrderedItemPairs = dashboardService.getFrequentlyOrderedItemPairs(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), frequentlyOrderedItemPairs));
    }

    @GetMapping("/most-used/{companyId}")
    public ResponseEntity<ApiResponse<List<DiscountUsageDTO>>> getMostUsedDiscountCodes(
            @PathVariable Long companyId,
            @RequestParam String period
    ) {
        List<DiscountUsageDTO> data = dashboardService.getMostUsedDiscountCodes(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), data));
    }

    @GetMapping("/filter-orders/{companyId}")
    public ResponseEntity<ApiResponse<List<Order>>> filterOrdersByTimeTaken(
            @PathVariable Long companyId,
            @RequestParam String timeTaken,
            @RequestParam String period) {
        List<Order> orders = dashboardService.filterOrdersByTimeTaken(companyId, timeTaken, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), orders));
    }
    @GetMapping("/filter-food-item-order-details/{companyId}")
    public ResponseEntity<ApiResponse<List<FoodItemOrderDetail>>> filterFoodItemOrderDetailsByTimeTaken(
            @PathVariable Long companyId,
            @RequestParam String timeTaken,
            @RequestParam String period) {
        List<FoodItemOrderDetail> details = dashboardService.filterFoodItemOrderDetailsByTimeTaken(companyId, timeTaken, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), details));
    }

    @GetMapping("/last-10-orders/{companyId}")
    public ResponseEntity<ApiResponse<List<Order>>> getLast10Orders(
            @PathVariable Long companyId
    ) {
        List<Order> last10Orders = dashboardService.getLast10Orders(companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), last10Orders));
    }
    @GetMapping("/last-10-bills/{companyId}")
    public ResponseEntity<ApiResponse<List<Bill>>> getLast10Bills(
            @PathVariable Long companyId
    ) {
        List<Bill> last10Bills = dashboardService.getLast10Bills(companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), last10Bills));
    }
    @GetMapping("/employee-performance/{companyId}")
    public ResponseEntity<ApiResponse<List<EmployeePerformanceDTO>>> getEmployeePerformance(
            @PathVariable Long companyId,
            @RequestParam String period
    ) {
        List<EmployeePerformanceDTO> employeePerformance = dashboardService.getEmployeePerformance(companyId, period);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), employeePerformance));
    }

    @GetMapping("/most-used-ingredients/{companyId}")
    public ResponseEntity<List<IngredientUsageDTO>> getMostUsedIngredients(@PathVariable Long companyId,@RequestParam String period) {
        return ResponseEntity.ok(dashboardService.getMostUsedIngredients(companyId,period));
    }

    @GetMapping("/least-used-ingredients/{companyId}")
    public ResponseEntity<List<IngredientUsageDTO>> getLeastUsedIngredients(@PathVariable Long companyId,@RequestParam String period) {
        return ResponseEntity.ok(dashboardService.getLeastUsedIngredients(companyId,period));
    }
}

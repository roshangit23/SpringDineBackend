package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.controller.DTO.OrderRequestDTO;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import com.hotelPro.hotelmanagementsystem.model.FoodItemOrderDetail;
import com.hotelPro.hotelmanagementsystem.model.Order;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public interface OrderService {
    Order saveNewOrder(OrderRequestDTO orderRequestDTO, Long companyId);
    Order saveOrder(OrderRequestDTO orderRequestDTO,  Long orderId,Long employeeId);
    Order getOrderById(Long id);
    List<Order> getAllOrders(Long companyId);
    void deleteOrder(Long id);
    Order findById(Long id);
    double calculateTotal(Order order);
    Order addFoodItemToOrder(Long orderId, Long foodItemId, int quantity);
    Order removeFoodItemFromOrder(Long orderId, Long foodItemId, int quantity);
    Order updateOrderComment(Long orderId, String comment);
    Order updateFoodItemOrderComment(Long orderId, Long foodItemId, String comment);
    Order updateOrderStatus(Long orderId, String status);
    public double calculateDiscount(Order order, Discount discount);

    List<Order> getOrdersByStatus(Long companyId, Order.Status status);

    List<Order> getOrdersByType(Long companyId, Order.OrderType orderType);

    Set<FoodItemOrder> getAllFoodItemOrdersByOrderId(Long orderId);
    Set<FoodItemOrder> getAllFoodItemOrdersByCompanyId(Long companyId);
    Set<FoodItemOrder> getFoodItemOrdersByStatusAndCompanyId(Long companyId,FoodItemOrder.Status status);
    FoodItemOrder updateFoodItemOrderStatus(Long orderId, Long foodItemId, Long foodItemOrderDetailId, FoodItemOrder.Status status);
    Duration getTimeTakenForFoodItemOrder(Long orderId, Long foodItemId);

    Set<FoodItemOrderDetail> getAllFoodItemOrderDetailsByCompanyId(Long companyId);
    Set<FoodItemOrderDetail> getAllFoodItemOrderDetailsByOrderId(Long orderId);
    Set<FoodItemOrderDetail> getFoodItemOrderDetailsByStatusAndCompanyId(Long companyId, FoodItemOrder.Status status);
    Duration getTimeTakenForFoodItemOrderDetail(Long orderId, Long foodItemId, Long foodItemOrderDetailId);
    FoodItemOrderDetail getFoodItemOrderDetailById(Long foodItemOrderDetailId);
}

package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.controller.DTO.OrderRequestDTO;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.Order;

import java.util.List;

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
}

package com.hotelPro.hotelmanagementsystem.service.operations;

import com.hotelPro.hotelmanagementsystem.model.FoodItem;
import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.service.FoodItemOrderService;
import com.hotelPro.hotelmanagementsystem.service.FoodItemService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderOps {

    private final OrderService orderService;
    private final FoodItemService foodItemService;

    private final FoodItemOrderService foodItemOrderService;

    public OrderOps(OrderService orderService, FoodItemService foodItemService, FoodItemOrderService foodItemOrderService) {
        this.orderService = orderService;
        this.foodItemService = foodItemService;
        this.foodItemOrderService = foodItemOrderService;
    }

    public void createOrder(Order order) {
        // implement the logic to create an order
        // Inserting Orders and Bills
        FoodItem foodItem3 = foodItemService.findById(3L); // assuming foodItem1 has id 1
        FoodItem foodItem4 = foodItemService.findById(5L); // assuming foodItem2 has id 2

         //orderService.saveOrder(order); // This will generate an ID for order

        //Now that order has an ID, you can use it when creating FoodItemOrder
        FoodItemOrder foodItemOrder1 = new FoodItemOrder(foodItem3, order, 2);
        FoodItemOrder foodItemOrder2 = new FoodItemOrder(foodItem4, order, 3);

        // Save FoodItemOrders
        foodItemOrderService.saveFoodItemOrder(foodItemOrder1);
        foodItemOrderService.saveFoodItemOrder(foodItemOrder2);
        System.out.println(foodItemOrder1);
        System.out.println(foodItemOrder2);
        System.out.println("saved food Item order successfully");
        System.out.println("Saved order successfully "+order);
    }

    public  void getAllOrders() {
        // implement the logic to get all orders
         //orderService.getAllOrders();
       //  System.out.println(orderService.getAllOrders());
    }

    public void getOrderById(Long id) {
        // implement the logic to get order by id
         System.out.println(orderService.getOrderById(id));
    }

    public void deleteOrder(Long id) {
        // implement the logic to delete an order
       // orderService.deleteOrder(id);
        System.out.print("Order deleted successfully for "+id);
    }

    public void addItemsToOrder(Long orderId, Long foodItemId,int quantity){
        orderService.addFoodItemToOrder(orderId,foodItemId,quantity);
    }

    public void removeItemsFromOrder(Long orderId, Long foodItemId,int quantity) {
        orderService.removeFoodItemFromOrder(orderId,foodItemId,quantity);
    }
}

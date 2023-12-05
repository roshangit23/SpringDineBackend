package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;
import com.hotelPro.hotelmanagementsystem.filter.ValidateOrderRequest;
import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import com.hotelPro.hotelmanagementsystem.model.FoodItemOrderDetail;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{orderId}")
    @ValidateOrderRequest
    public ResponseEntity<ApiResponse<OrderResponseDTO>> saveOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO, @PathVariable Long orderId) {
        Order savedOrder = orderService.saveOrder(orderRequestDTO, orderId, null);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(savedOrder)));
    }

    @PostMapping("/newOrder/{companyId}")
    @ValidateOrderRequest
    public ResponseEntity<ApiResponse<OrderResponseDTO>> saveNewOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO, @PathVariable Long companyId) {
        Order savedOrder = orderService.saveNewOrder(orderRequestDTO, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(savedOrder)));
    }

    @PostMapping("/saveWithEmployee/{orderId}/{employeeId}")
    @ValidateOrderRequest
    public ResponseEntity<ApiResponse<OrderResponseDTO>> saveOrderWithEmployee(@Valid @RequestBody OrderRequestDTO orderRequestDTO, @PathVariable Long orderId, @PathVariable Long employeeId) {
        Order savedOrder = orderService.saveOrder(orderRequestDTO, orderId, employeeId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(savedOrder)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(order)));
    }

    @GetMapping("/getByOrderNo/{companyId}/{orderNo}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderByOrderNoAndCompanyId(@PathVariable Long companyId, @PathVariable Long orderNo) {
        Order order = orderService.findByOrderNoAndCompanyId(orderNo, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(order)));
    }
    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders(@PathVariable Long companyId) {
        List<Order> orders = orderService.getAllOrders(companyId);
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), orderDTOs));
    }

    @PutMapping("/addFoodItem/{orderId}/{foodItemId}/{quantity}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> addFoodItemToOrder(@PathVariable Long orderId, @PathVariable Long foodItemId, @PathVariable int quantity) {
        Order order = orderService.addFoodItemToOrder(orderId, foodItemId, quantity);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(order)));
    }

    @PutMapping("removeFoodItem/{orderId}/{foodItemId}/{quantity}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> removeFoodItemFromOrder(@PathVariable Long orderId, @PathVariable Long foodItemId, @PathVariable int quantity) {
        Order order = orderService.removeFoodItemFromOrder(orderId, foodItemId, quantity);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(order)));
    }

    @PutMapping("/comment/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderComment(@PathVariable(value = "orderId") Long orderId, @RequestBody Map<String, String> comment) {
        Order order = orderService.updateOrderComment(orderId, comment.get("comment"));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(order)));
    }

    @PutMapping("/comment/{orderId}/{foodItemId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateFoodItemOrderComment(@PathVariable(value = "orderId") Long orderId, @PathVariable(value = "foodItemId") Long foodItemId, @RequestBody Map<String, String> comment) {
        Order order = orderService.updateFoodItemOrderComment(orderId, foodItemId, comment.get("comment"));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(order)));
    }

    @PutMapping("/status/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> status) {
        Order order = orderService.updateOrderStatus(orderId, status.get("status"));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new OrderResponseDTO(order)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id, @Valid @RequestBody DeleteBillRequest request) {
        String comments = request.getComments();
        orderService.deleteOrder(id,comments);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Order deleted successfully"));
    }

    @GetMapping("/duration/{orderId}")
    public ResponseEntity<ApiResponse<String>> getOrderDuration(@PathVariable Long orderId) {
        Order order = orderService.findById(orderId);

        if (order == null) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Order not found"), HttpStatus.NOT_FOUND);
        }

        LocalDateTime orderPlacedAt = order.getStartTime();
        LocalDateTime orderCompletedAt = order.getEndTime();

        // If the order is still in progress, use the current time
        LocalDateTime endTime = (orderCompletedAt != null) ? orderCompletedAt : LocalDateTime.now();

        Duration duration = Duration.between(orderPlacedAt, endTime);
        long durationInMinutes = duration.toMinutes();

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Order duration: " + durationInMinutes + " minutes"));
    }

    @GetMapping("/getOrdersByStatus/{companyId}/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersByStatus(@PathVariable Long companyId,@PathVariable Order.Status status) {
        List<Order> orders = orderService.getOrdersByStatus(companyId,status);
       // return ResponseEntity.ok(orders);

        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), orderDTOs));
    }

    @GetMapping("/getOrdersByType/{companyId}/{type}")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersByType(@PathVariable Long companyId,@PathVariable Order.OrderType type) {
        List<Order> orders = orderService.getOrdersByType(companyId,type);
        // return ResponseEntity.ok(orders);

        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), orderDTOs));
    }

    //FoodItemOrder APIs
    @GetMapping("/getAllFoodItemOrders/{companyId}")
    public ResponseEntity<ApiResponse<Set<FoodItemOrderResponseDTO>>> getAllFoodItemOrders(@PathVariable Long companyId) {
        Set<FoodItemOrder> foodItemOrders = orderService.getAllFoodItemOrdersByCompanyId(companyId);
        Set<FoodItemOrderResponseDTO> responseDTOs = foodItemOrders.stream()
                .map(FoodItemOrderResponseDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTOs));
    }

    @GetMapping("/foodItemOrders/{orderId}")
    public ResponseEntity<ApiResponse<Set<FoodItemOrderResponseDTO>>> getAllFoodItemOrdersByOrderId(@PathVariable Long orderId) {
        Set<FoodItemOrder> foodItemOrders = orderService.getAllFoodItemOrdersByOrderId(orderId);
        Set<FoodItemOrderResponseDTO> responseDTOs = foodItemOrders.stream()
                .map(FoodItemOrderResponseDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTOs));
    }
    @GetMapping("/foodItemOrderById/{foodItemOrderId}")
    public ResponseEntity<ApiResponse<FoodItemOrderResponseDTO>> getAllFoodItemOrdersByfoodItemOrderId(@PathVariable Long foodItemOrderId) {
        FoodItemOrder foodItemOrder = orderService.getFoodItemOrderById(foodItemOrderId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new FoodItemOrderResponseDTO(foodItemOrder)));
    }

    @GetMapping("/foodItemOrdersByStatus/{companyId}/{status}")
    public ResponseEntity<ApiResponse<Set<FoodItemOrderResponseDTO>>> getFoodItemOrdersByStatus(@PathVariable Long companyId, @PathVariable FoodItemOrder.Status status) {
        Set<FoodItemOrder> foodItemOrders = orderService.getFoodItemOrdersByStatusAndCompanyId(companyId, status);
        Set<FoodItemOrderResponseDTO> responseDTOs = foodItemOrders.stream()
                .map(FoodItemOrderResponseDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTOs));
    }

    @PutMapping("/foodItemOrder/status/{orderId}/{foodItemId}/{foodItemOrderDetailId}")
    public ResponseEntity<ApiResponse<CombinedFoodItemOrderResponseDTO>> updateFoodItemOrderStatus(
            @PathVariable Long orderId,
            @PathVariable Long foodItemId,
            @PathVariable Long foodItemOrderDetailId,
            @Valid @RequestBody FoodItemOrderStatusUpdateDTO statusUpdateDTO) {
        FoodItemOrder updatedFoodItemOrder = orderService.updateFoodItemOrderStatus(orderId, foodItemId, foodItemOrderDetailId, statusUpdateDTO.getStatus());
        CombinedFoodItemOrderResponseDTO responseDTO = new CombinedFoodItemOrderResponseDTO(updatedFoodItemOrder);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTO));
    }


    @GetMapping("/foodItemOrder/duration/{orderId}/{foodItemId}")
    public ResponseEntity<ApiResponse<DurationResponseDTO>> getTimeTakenForFoodItemOrder(@PathVariable Long orderId, @PathVariable Long foodItemId) {
        Duration duration = orderService.getTimeTakenForFoodItemOrder(orderId, foodItemId);
        long durationInMinutes = duration.toMinutes();
        DurationResponseDTO responseDTO = new DurationResponseDTO("FoodItemOrder duration: " + durationInMinutes + " minutes", durationInMinutes);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTO));
    }

    //FoodItemOrderDetail APIs
    @GetMapping("/getAllFoodItemOrderDetails/{companyId}")
    public ResponseEntity<ApiResponse<Set<FoodItemOrderDetailResponseDTO>>> getAllFoodItemOrderDetailsByCompanyId(@PathVariable Long companyId) {
        Set<FoodItemOrderDetail> foodItemOrderDetails = orderService.getAllFoodItemOrderDetailsByCompanyId(companyId);
        Set<FoodItemOrderDetailResponseDTO> responseDTOs = foodItemOrderDetails.stream()
                .map(FoodItemOrderDetailResponseDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTOs));
    }

    @GetMapping("/foodItemOrderDetails/{orderId}")
    public ResponseEntity<ApiResponse<Set<FoodItemOrderDetailResponseDTO>>> getAllFoodItemOrderDetailsByOrderId(@PathVariable Long orderId) {
        Set<FoodItemOrderDetail> foodItemOrderDetails = orderService.getAllFoodItemOrderDetailsByOrderId(orderId);
        Set<FoodItemOrderDetailResponseDTO> responseDTOs = foodItemOrderDetails.stream()
                .map(FoodItemOrderDetailResponseDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTOs));
    }

    @GetMapping("/foodItemOrderDetailsByStatus/{companyId}/{status}")
    public ResponseEntity<ApiResponse<Set<FoodItemOrderDetailResponseDTO>>> getFoodItemOrderDetailsByStatusAndCompanyId(@PathVariable Long companyId, @PathVariable FoodItemOrder.Status status) {
        Set<FoodItemOrderDetail> foodItemOrderDetails = orderService.getFoodItemOrderDetailsByStatusAndCompanyId(companyId, status);
        Set<FoodItemOrderDetailResponseDTO> responseDTOs = foodItemOrderDetails.stream()
                .map(FoodItemOrderDetailResponseDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTOs));
    }

    @GetMapping("/foodItemOrderDetail/duration/{orderId}/{foodItemId}/{foodItemOrderDetailId}")
    public ResponseEntity<ApiResponse<DurationResponseDTO>> getTimeTakenForFoodItemOrderDetail(@PathVariable Long orderId, @PathVariable Long foodItemId, @PathVariable Long foodItemOrderDetailId) {
        Duration duration = orderService.getTimeTakenForFoodItemOrderDetail(orderId, foodItemId, foodItemOrderDetailId);
        long durationInMinutes = duration.toMinutes();
        DurationResponseDTO responseDTO = new DurationResponseDTO("FoodItemOrderDetail duration: " + durationInMinutes + " minutes", durationInMinutes);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), responseDTO));
    }

}


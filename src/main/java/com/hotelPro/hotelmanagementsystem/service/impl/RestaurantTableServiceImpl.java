package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.repository.*;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import com.hotelPro.hotelmanagementsystem.service.RestaurantTableService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FoodItemOrderRepository foodItemOrderRepository;
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CompanyRepository companyRepository;
    @Override
    @Transactional
    public RestaurantTable createTable(String category, int tableNumber, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        // Check for uniqueness constraint
        Optional<RestaurantTable> existingTable = restaurantTableRepository.findByCategoryAndTableNumber(category, tableNumber);
        if (existingTable.isPresent()) {
            throw new CustomException("A table with this category and table number already exists", HttpStatus.BAD_REQUEST);
        }

        RestaurantTable restaurantTable = new RestaurantTable(RestaurantTable.TableStatus.FREE, category, tableNumber, null);

        restaurantTable.setCompany(company);
        return restaurantTableRepository.save(restaurantTable);
    }

    @Override
    @Transactional
    public RestaurantTable createOrderForTable(Long tableId) {
//        Company company = companyRepository.findById(companyId)
//                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", tableId));
        if (table.getStatus() != RestaurantTable.TableStatus.FREE) {
            throw new CustomException("Table is not free, please select another table.", HttpStatus.CONFLICT);
        }
        Order order = new Order();
        order.setCompany(table.getCompany());
        // Generate unique orderNo for the order within the company
        Long lastOrderNo = orderRepository.findMaxOrderNoByCompany(table.getCompany().getId());
        order.setOrderNo(lastOrderNo == null ? 1 : lastOrderNo + 1);

        order.setStatus(Order.Status.IN_PROGRESS);
        order.setStartTime(LocalDateTime.now());
        order.setType(Order.OrderType.DINE_IN);
        table.setCurrentOrder(order);
       // order.setRestaurantTable(table);
        table.setStatus(RestaurantTable.TableStatus.OCCUPIED);

      return restaurantTableRepository.save(table);
    }

    @Override
    @Transactional
    public List<RestaurantTable> checkAndFreeTablesIfEmpty(Long companyId) {
        // Only fetch tables that have a current order and belong to the specified companyId
        List<RestaurantTable> tables = restaurantTableRepository.findByCurrentOrderIsNotNullAndCompanyId(companyId);
        for (RestaurantTable table : tables) {
            Order order = table.getCurrentOrder();
            if (order.getStatus() == Order.Status.IN_PROGRESS && order.getFoodItemOrders().isEmpty()) {
                order.setStatus(Order.Status.REMOVED_WITHOUT_CREATING);
                order.setEndTime(LocalDateTime.now());
                orderRepository.save(order);
                table.setStatus(RestaurantTable.TableStatus.FREE);
                table.setCurrentOrder(null);
            }
        }

        return tables;
    }



    @Override
    @Transactional
    public RestaurantTable occupyTable(Long tableId) {
        RestaurantTable table = getTableById(tableId);
        if (table.getCurrentOrder() == null || table.getCurrentOrder().getStatus() != Order.Status.IN_PROGRESS) {
            throw new CustomException("Table can only be occupied if an order against that table is created.", HttpStatus.BAD_REQUEST);
        }
        table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
        return restaurantTableRepository.save(table);
    }
    @Override
    @Transactional
    public RestaurantTable completeTable(Long tableId) {
        RestaurantTable table = getTableById(tableId);
        if((table.getCurrentOrder() == null) || (table.getCurrentOrder().getStatus() != Order.Status.COMPLETED && table.getCurrentOrder().getStatus() != Order.Status.REMOVED_WITHOUT_CREATING)) {
            throw new CustomException("Table can only be completed if the current order is marked as COMPLETE or REMOVED_WITHOUT_CREATING.", HttpStatus.BAD_REQUEST);
        }

        table.setStatus(RestaurantTable.TableStatus.COMPLETED);
        return restaurantTableRepository.save(table);
    }
//    @Override
//    @Transactional
//    public RestaurantTable freeTable(Long tableId) {
//        RestaurantTable table = getTableById(tableId);
//        Order currentOrder = table.getCurrentOrder();
//        if ((!table.getBills().stream().allMatch(bill -> bill.getStatus() == Bill.BillStatus.SETTLED || bill.getStatus() == Bill.BillStatus.NOT_SETTLED))
//            || (currentOrder == null || (currentOrder.getStatus() != Order.Status.MERGED && currentOrder.getStatus() != Order.Status.REMOVED_WITHOUT_CREATING
//        && currentOrder.getStatus() != Order.Status.COMPLETED))) {
//            throw new CustomException("Table can only be freed when all bills are SETTLED or NOT_SETTLED, and the current order is in an appropriate state.", HttpStatus.BAD_REQUEST);
//        }
//        currentOrder.setEndTime(LocalDateTime.now());
//        table.setStatus(RestaurantTable.TableStatus.FREE);
//        table.setCurrentOrder(null); // Clear the current order when freeing the table
//        return restaurantTableRepository.save(table);
//    }
@Override
@Transactional
public RestaurantTable freeTable(Long tableId) {
    RestaurantTable table = getTableById(tableId);
    Order currentOrder = table.getCurrentOrder();
    Bill currentBill = billRepository.findFirstByRestaurantTableOrderByBillCreatedTimeDesc(table);
    if(currentOrder != null){
    if(currentOrder.getStatus() == Order.Status.MERGED || currentOrder.getStatus() == Order.Status.COMPLETED) {
       if (currentBill == null || (currentBill.getStatus() != Bill.BillStatus.SETTLED && currentBill.getStatus() != Bill.BillStatus.NOT_SETTLED)) {
           throw new CustomException("The current bill must be SETTLED or NOT_SETTLED to free the table.", HttpStatus.BAD_REQUEST);
       }
     }
    }
    if(currentOrder != null) {
        if (currentOrder.getStatus() != Order.Status.MERGED && currentOrder.getStatus() != Order.Status.REMOVED_WITHOUT_CREATING
                && currentOrder.getStatus() != Order.Status.COMPLETED) {
            throw new CustomException("Table can only be freed when the current order is in an appropriate state.", HttpStatus.BAD_REQUEST);
        }
    }
    if(currentOrder != null) {
        currentOrder.setEndTime(LocalDateTime.now());
    }
    table.setCurrentOrder(null); // Clear the current order when freeing the table
    table.setStatus(RestaurantTable.TableStatus.FREE);
    return restaurantTableRepository.save(table);
}

    @Override
    @Transactional
    public RestaurantTable.TableStatus getTableStatusById(Long tableId) {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantTable", "id", tableId));
        return restaurantTable.getStatus();
    }
    @Override
    @Transactional
    public RestaurantTable getTableById(Long tableId) {
        return restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", tableId));
    }

    @Override
    @Transactional
    public RestaurantTable updateTable(Long tableId, String category, Integer tableNumber) {
        RestaurantTable restaurantTable = getTableById(tableId);

        if (category != null && !category.isEmpty()) {
            restaurantTable.setCategory(category);
        }

        if (tableNumber != null) {
            restaurantTable.setTableNumber(tableNumber);
        }

        return restaurantTableRepository.save(restaurantTable);
    }

//    @Override
//    public RestaurantTable getTableByOrderId(Long id) {
////        return restaurantTableRepository.findByCurrentOrder(id)
////                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
//    }
@Override
@Transactional
public double mergeOrders(List<Long> tableIds) {
    List<RestaurantTable> tables = tableIds.stream()
            .map(id -> restaurantTableRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Table with id " + id + " does not exist.", HttpStatus.BAD_REQUEST)))
            .collect(Collectors.toList());

    List<Long> tableIdsWithoutInProgressOrders = new ArrayList<>();
    List<Order> inProgressOrders = new ArrayList<>();
    for (RestaurantTable table : tables) {
        Order order = table.getCurrentOrder();
        if (order == null || order.getStatus() != Order.Status.IN_PROGRESS) {
            tableIdsWithoutInProgressOrders.add(table.getId());
        } else {
            inProgressOrders.add(order);
        }
    }

    if (!tableIdsWithoutInProgressOrders.isEmpty()) {
        throw new CustomException("Tables with ids " + tableIdsWithoutInProgressOrders + " do not have an in-progress order.", HttpStatus.BAD_REQUEST);
    }

    // Merge the food item orders and calculate the total amount
    List<FoodItemOrder> allFoodItemOrders = new ArrayList<>();
    double totalAmount = 0.0;
    for (Order order : inProgressOrders) {
        allFoodItemOrders.addAll(order.getFoodItemOrders());
        totalAmount += orderService.calculateTotal(order);
    }

    // Remove food item orders from all tables except the first one
    Order mainOrder = inProgressOrders.get(0);
    for (int i = 1; i < inProgressOrders.size(); i++) {
        Order orderToRemove = inProgressOrders.get(i);
        foodItemOrderRepository.deleteAll(orderToRemove.getFoodItemOrders());
        orderToRemove.getFoodItemOrders().clear();

        // Update the order status to MERGED
        orderToRemove.setStatus(Order.Status.MERGED);

        // Update the table status to FREE and clear the current order
        RestaurantTable tableToFree = tables.get(i);
        tableToFree.setCurrentOrder(null);
        tableToFree.setStatus(RestaurantTable.TableStatus.FREE);

        // Save the order and the table
        orderRepository.save(orderToRemove);
        restaurantTableRepository.save(tableToFree);
    }

    // Add all food item orders to the main order and save
    mainOrder.getFoodItemOrders().addAll(allFoodItemOrders);
    orderRepository.save(mainOrder);

    return totalAmount;
}

    @Override
    @Transactional
    public List<RestaurantTable> getAllTables(Long companyId) {
        return restaurantTableRepository.findByCompanyId(companyId);
    }

}

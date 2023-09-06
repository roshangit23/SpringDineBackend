package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.controller.DTO.FoodItemOrderDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.OrderRequestDTO;
import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.InvalidEnumValueException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.repository.*;
import com.hotelPro.hotelmanagementsystem.service.CustomerService;
import com.hotelPro.hotelmanagementsystem.service.FoodItemService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private FoodItemOrderRepository foodItemOrderRepository;

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private FoodItemService foodItemService;
//    @Autowired
//    private RestaurantTableService restaurantTableService;
   @Autowired
   private CompanyRepository companyRepository;

    private Order convertToEntity(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        if(orderRequestDTO.getComments()!=null){
            order.setComments(orderRequestDTO.getComments());
        }
        order.setStatus(Order.Status.IN_PROGRESS);
        if(orderRequestDTO.getCustomer_count()!=null){
            order.setCustomer_count(orderRequestDTO.getCustomer_count());
        }
        if (orderRequestDTO.getType() != null && !orderRequestDTO.getType().trim().isEmpty()) {
            try {
                order.setType(Order.OrderType.valueOf(orderRequestDTO.getType()));
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumValueException("type","Invalid value for type");
            }
        }

        // For customerId and employeeId we can't set directly to the order
        // we need to fetch the corresponding Customer and Employee entities from the database
        // and set them to the order
        if(orderRequestDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderRequestDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", orderRequestDTO.getCustomerId()));
            order.setCustomer(customer);
        }

        if(orderRequestDTO.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(orderRequestDTO.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", orderRequestDTO.getEmployeeId()));
            order.setAssignedEmployee(employee);
        }

        // Set<FoodItemOrder> requires a bit more complex handling
        // As we can't just set DTO fields into entity
        // We should create new FoodItemOrder entities with appropriate links
        // Here we suppose that FoodItemOrderDTO has a field foodItemId to link it with FoodItem

        if (orderRequestDTO.getFoodItemOrders() != null) {
            Set<FoodItemOrder> foodItemOrders = orderRequestDTO.getFoodItemOrders().stream()
                    .map(this::convertFoodItemOrderToEntity)   // calling the conversion method for FoodItemOrder
                    .collect(Collectors.toSet());
            order.setFoodItemOrders(foodItemOrders);
        }else {
            order.setFoodItemOrders(new HashSet<>());
        }

        return order;
    }

    private void updateOrderFromDTO(Order order, OrderRequestDTO orderRequestDTO) {
        // You already have the logic for this in your convertToEntity function.
        // Extract the updating parts to this method.
        if(orderRequestDTO.getComments()!=null){
            order.setComments(orderRequestDTO.getComments());
        }
        if(orderRequestDTO.getCustomer_count()!=null){
            order.setCustomer_count(orderRequestDTO.getCustomer_count());
        }
        if (orderRequestDTO.getType() != null && !orderRequestDTO.getType().trim().isEmpty()) {
            try {
                order.setType(Order.OrderType.valueOf(orderRequestDTO.getType()));
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumValueException("type", "Invalid value for type");
            }
        }

        // For customerId and employeeId we can't set directly to the order
        // we need to fetch the corresponding Customer and Employee entities from the database
        // and set them to the order
        if (orderRequestDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderRequestDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", orderRequestDTO.getCustomerId()));
            order.setCustomer(customer);
        }

        if (orderRequestDTO.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(orderRequestDTO.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", orderRequestDTO.getEmployeeId()));
            order.setAssignedEmployee(employee);
        }

        // Set<FoodItemOrder> requires a bit more complex handling
        // As we can't just set DTO fields into entity
        // We should create new FoodItemOrder entities with appropriate links
        // Here we suppose that FoodItemOrderDTO has a field foodItemId to link it with FoodItem

        if (orderRequestDTO.getFoodItemOrders() != null) {
            for (FoodItemOrderDTO foodItemOrderDTO : orderRequestDTO.getFoodItemOrders()) {
                // Throw exception if foodItemId or quantity is null
                if (foodItemOrderDTO.getFoodItemId() == null) {
                    throw new CustomException("FoodItemId cannot be null", HttpStatus.BAD_REQUEST);
                }
                if (foodItemOrderDTO.getQuantity() == null) {
                    throw new CustomException("Quantity cannot be null", HttpStatus.BAD_REQUEST);
                }
                // Check if a FoodItemOrder with the same foodItemId already exists in the order
                Optional<FoodItemOrder> existingOrder = order.getFoodItemOrders()
                        .stream()
                        .filter(fio -> fio.getFoodItem().getId().equals(foodItemOrderDTO.getFoodItemId()))
                        .findFirst();

                if (existingOrder.isPresent()) {
                    // Update its quantity
                    existingOrder.get().setQuantity(existingOrder.get().getQuantity() + foodItemOrderDTO.getQuantity());
                    if (foodItemOrderDTO.getComments() != null) {
                        existingOrder.get().setComments(foodItemOrderDTO.getComments());
                    }
                } else {
                    // If it doesn't exist, add a new FoodItemOrder
                    FoodItemOrder newFoodItemOrder = convertFoodItemOrderToEntity(foodItemOrderDTO);
                    order.getFoodItemOrders().add(newFoodItemOrder);
                }
            }
        }
    }

    private FoodItemOrder convertFoodItemOrderToEntity(FoodItemOrderDTO foodItemOrderDTO) {
        FoodItemOrder foodItemOrder = new FoodItemOrder();
        if(foodItemOrderDTO.getQuantity()==null){
            throw new CustomException("'quantity' for foodItemOrders is mandatory", HttpStatus.BAD_REQUEST);
        }
        if(foodItemOrderDTO.getFoodItemId()==null){
            throw new CustomException("'foodItemId' for foodItemOrders is mandatory", HttpStatus.BAD_REQUEST);
        }
        foodItemOrder.setQuantity(foodItemOrderDTO.getQuantity());
        foodItemOrder.setComments(foodItemOrderDTO.getComments());

        FoodItem foodItem = foodItemService.findById(foodItemOrderDTO.getFoodItemId());
        foodItemOrder.setFoodItem(foodItem);

        return foodItemOrder;
    }

    @Override
    @Transactional
    public Order saveNewOrder(OrderRequestDTO orderRequestDTO, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        //return saveOrder(orderRequestDTO,orderId, null);
      Order order = convertToEntity(orderRequestDTO);
        order.setStartTime(LocalDateTime.now());  // Set the start time for new orders
        order.setCompany(company);
        return order;
    }
    @Override
    @Transactional
    public Order saveOrder(OrderRequestDTO orderRequestDTO, Long orderId, Long employeeId) {
        Order order;

        // If orderId is provided, then it's an update. Fetch the existing order.
        if(orderId != null) {
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
            if(order.getStatus()== Order.Status.COMPLETED || order.getStatus()== Order.Status.MERGED || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
                throw new CustomException("Order cannot be updated once order is completed, merged or removed", HttpStatus.BAD_REQUEST);
            }
            if (order.getBill() != null) {
                throw new CustomException("Cannot update order that is already billed",HttpStatus.BAD_REQUEST);
            }
            updateOrderFromDTO(order, orderRequestDTO);  // Update the existing order with the DTO data
        } else {
            // Else, create a new order
            order = convertToEntity(orderRequestDTO);
            order.setStartTime(LocalDateTime.now());  // Set the start time for new orders
        }
//        if(order.getType()==Order.OrderType.DINE_IN){
//            order.setRestaurantTable();
//        }
        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
            order.setAssignedEmployee(employee);
        }

        // Check if customer details are mandatory
        if (order.getType() == Order.OrderType.DELIVERY) {
            // Check for existing customer by phone number
            if (order.getCustomer() == null) {
                throw new CustomException("Order does not have an associated customer", HttpStatus.BAD_REQUEST);
            }
        }

        if (order.getCustomer() != null) {
            Customer customer = null;
            if (order.getCustomer().getPhoneNumber() != null) {
                customer = customerRepository.findByPhoneNumber(order.getCustomer().getPhoneNumber());
                if(customer!=null){
                    if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                        throw new CustomException("Name is mandatory", HttpStatus.BAD_REQUEST);
                    }

                    // If the order type is delivery, validate that the  phone number and address is not blank
                    if (order.getType()  == Order.OrderType.DELIVERY) {
                        if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                            throw new CustomException("Address is mandatory for delivery orders", HttpStatus.BAD_REQUEST);
                        }
                    }
                }
            }
            if (customer == null) {
                customer = customerService.saveCustomerForOrder(order.getCustomer(), order.getType());
            }
            order.setCustomer(customer);
        }

        if (order.getFoodItemOrders() != null) {
            for (FoodItemOrder foodItemOrder : order.getFoodItemOrders()) {
                foodItemOrder.setOrder(order);  // set the saved Order in the FoodItemOrder
            }
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order getOrderById(Long id) {
        //Optional<Order> optionalOrder = orderRepository.findById(id);
        //return optionalOrder.orElse(null);
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @Override
    @Transactional
    public List<Order> getAllOrders(Long companyId) {
        return orderRepository.findByCompanyId(companyId);
        //return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        if(order.getStatus()== Order.Status.IN_PROGRESS || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
            throw new CustomException("Order cannot be deleted once order is completed, merged or removed", HttpStatus.BAD_REQUEST);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @Override
    @Transactional
    public double calculateTotal(Order order) {
        double total = 0.0;
        Set<FoodItemOrder> foodItemOrders = order.getFoodItemOrders();
        System.out.println("***********************************************"+foodItemOrders.size()+"***********************************************");

        for (FoodItemOrder itemOrder : foodItemOrders) {
            total += itemOrder.getQuantity() * itemOrder.getFoodItem().getItemPrice();
            System.out.println("***********************************************"+total+"***********************************************");

        }
        return total;
    }

    @Override
    public Order addFoodItemToOrder(Long orderId, Long foodItemId,int quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if(order.getStatus()== Order.Status.COMPLETED || order.getStatus()== Order.Status.MERGED || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
            throw new CustomException("Order cannot be updated once order is completed, merged or removed", HttpStatus.BAD_REQUEST);
        }
        // Check if a Bill already exists for this Order
        Optional<Bill> existingBill = billRepository.findByOrderId(orderId);
        if (existingBill.isPresent()) {
            throw new CustomException("Cannot update Order as a Bill has already been generated for it.", HttpStatus.BAD_REQUEST);
        }

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "id", foodItemId));

        // Check if food item is already present in the order
        boolean itemFound = false;
        for (FoodItemOrder foodItemOrder : order.getFoodItemOrders()) {
            if (foodItemOrder.getFoodItem().getId().equals(foodItemId)) {
                foodItemOrder.setQuantity(foodItemOrder.getQuantity() + quantity);
                foodItemOrderRepository.save(foodItemOrder);
                itemFound = true;
                break;
            }
        }
        // If food item is not present in the order, add a new FoodItemOrder
        if (!itemFound) {
            FoodItemOrder foodItemOrder = new FoodItemOrder(foodItem, order, quantity);
            order.addFoodItemOrder(foodItemOrder);
            orderRepository.save(order);
        }

        return order;
    }

    @Override
    public Order removeFoodItemFromOrder(Long orderId, Long foodItemId, int quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if(order.getStatus()== Order.Status.COMPLETED || order.getStatus()== Order.Status.MERGED || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
            throw new CustomException("Order cannot be updated once order is completed, merged or removed", HttpStatus.BAD_REQUEST);
        }
        // Check if a Bill already exists for this Order
        Optional<Bill> existingBill = billRepository.findByOrderId(orderId);
        if (existingBill.isPresent()) {
            throw new CustomException("Cannot update Order as a Bill has already been generated for it.", HttpStatus.BAD_REQUEST);
        }

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", "id", foodItemId));

        boolean itemFound = false;
        for (FoodItemOrder foodItemOrder : order.getFoodItemOrders()) {
            if (foodItemOrder.getFoodItem().getId().equals(foodItemId)) {
                int quantityToUpdate = foodItemOrder.getQuantity() - quantity;
                if(quantityToUpdate>=0) {
                    foodItemOrder.setQuantity(quantityToUpdate);
                }
                else{
                    throw new CustomException("Cannot remove items as quantity for removal exceeds the existing quantity", HttpStatus.BAD_REQUEST);
                }
               if(foodItemOrder.getQuantity()<=0){
                    order.removeFoodItemOrder(foodItemOrder);
               }
                foodItemOrderRepository.save(foodItemOrder);
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            throw new ResourceNotFoundException("Food item "+foodItem.getItemName(), "id", orderId);
        }
        orderRepository.save(order);
        return order;
    }
    @Override
    public Order updateOrderComment(Long orderId, String comment) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if(order.getStatus()== Order.Status.COMPLETED || order.getStatus()== Order.Status.MERGED || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
            throw new CustomException("Order cannot be updated once order is completed, merged or removed", HttpStatus.BAD_REQUEST);
        }
        if(comment!=null){
            order.setComments(comment);
        }else{
            throw new CustomException("comment field missing in request body", HttpStatus.BAD_REQUEST);
        }
        return orderRepository.save(order);
    }
    @Override
    public Order updateFoodItemOrderComment(Long orderId, Long foodItemId, String comment) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if(order.getStatus()== Order.Status.COMPLETED || order.getStatus()== Order.Status.MERGED || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
            throw new CustomException("Order cannot be updated once order is completed, merged or removed", HttpStatus.BAD_REQUEST);
        }
        FoodItemOrder foodItemOrder = order.getFoodItemOrders().stream()
                .filter(fio -> fio.getFoodItem().getId().equals(foodItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("FoodItemOrder", "foodItemId", foodItemId));

        if(comment!=null){
            foodItemOrder.setComments(comment);
        }else{
            throw new CustomException("comment field missing in request body", HttpStatus.BAD_REQUEST);
        }
        return orderRepository.save(order);
    }
    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
//        if(order.getStatus()== Order.Status.COMPLETED || order.getStatus()== Order.Status.MERGED || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
//            throw new CustomException("Order cannot be updated once order is completed, merged or removed", HttpStatus.BAD_REQUEST);
//        }
        if (order.getBill() != null) {
            throw new CustomException("Cannot update order that is already billed",HttpStatus.BAD_REQUEST);
        }
       if(status== null){
           throw new CustomException("status field missing in request body", HttpStatus.BAD_REQUEST);
       }
    try {
        Order.Status orderStatus = Order.Status.valueOf(status.trim().toUpperCase());
        order.setStatus(orderStatus);
        // If the status is whatever you consider complete, set the end time
        if (orderStatus == Order.Status.COMPLETED || orderStatus == Order.Status.MERGED || orderStatus == Order.Status.REMOVED_WITHOUT_CREATING) {
            order.setEndTime(LocalDateTime.now());
        }
    } catch (IllegalArgumentException e) {
        throw new InvalidEnumValueException("status", "Invalid value for status");
    }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByStatus(Long companyId, Order.Status status){
        return orderRepository.findByCompany_IdAndStatus(companyId, status);
    }

    @Override
    public List<Order> getOrdersByType(Long companyId, Order.OrderType orderType){
        return orderRepository.findByCompany_IdAndType(companyId, orderType);
    }

    @Override
    public double calculateDiscount(Order order, Discount discount) {
        double discountAmount = 0.0;

        // Check if the discount is applicable for the specific order type
        if (discount.getApplicableOrderType() != null && discount.getApplicableOrderType() != order.getType()) {
            return discountAmount;
        }
//// Check if the discount is applicable for the specific order type
//        if (discount.getApplicableOrderType().isPresent()) {
//            Order.OrderType applicableOrderType = discount.getApplicableOrderType().get();
//            if (applicableOrderType != order.getType()) {
//                discountAmount = 0.0;
//            }
//        }

        // Check if the discount is applicable for the specific customer
        if (discount.getApplicableCustomers() != null && !discount.getApplicableCustomers().contains(order.getCustomer())) {
            return discountAmount;
        }

        // Check if the discount is applicable based on the minimum bill amount
        if (discount.getMinimumBillAmount() > 0 && calculateTotal(order) < discount.getMinimumBillAmount()) {
            return discountAmount;
        }

        // Calculate the discount amount
        discountAmount = (discount.getPercentage() / 100) * calculateTotal(order);

        return discountAmount;
    }

}

package com.hotelPro.hotelmanagementsystem.filter;

import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class EntityServiceResolver {

    @Autowired
    private BillService billService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantTableService tableService;

    public List<CompanyAssociatedEntity> resolveEntity(HttpServletRequest request) throws IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.startsWith("/bills/")) {
            return resolveBillEntity(request);
        } else if (path.startsWith("/customers/")) {
            return resolveCustomerEntity(request);
        } else if (path.startsWith("/discounts/")) {
            return resolveDiscountEntity(request);
        } else if (path.startsWith("/employees/")) {
            return resolveEmployeeEntity(request);
        } else if (path.startsWith("/foodItems/")) {
            return resolveFoodItemEntity(request);
        } else if (path.startsWith("/inventories/")) {
            return resolveInventoryEntity(request);
        } else if (path.startsWith("/orders/")) {
            return resolveOrderEntity(request);
        } else if (path.startsWith("/tables/")) {
            return resolveTableEntity(request);
        }

        return Collections.emptyList();
    }

    private List<CompanyAssociatedEntity> resolveBillEntity(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        List<CompanyAssociatedEntity> entities = new ArrayList<>();

        // Handle GET methods
        if ("GET".equalsIgnoreCase(method)) {
            // Pattern to match /bills/{id}
            if (path.matches("/bills/\\d+")) {
                Long billId = extractIdFromPath(path, 2);
                Bill bill = billService.getBillById(billId);
                if (bill != null) {
                    entities.add(bill);
                }
            }
        }
        // Handle POST methods
        else if ("POST".equalsIgnoreCase(method)) {
            // Pattern to match /bills/{orderId}
            if (path.matches("/bills/create/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                // Fetch the order using the orderId
                Order order = orderService.getOrderById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }
            else if (path.matches("/bills/create/\\d+/\\d+")) {
                Long orderId = Long.parseLong(path.split("/")[3]);
                Long discountId = Long.parseLong(path.split("/")[4]);
                Order order = orderService.getOrderById(orderId);
                Discount discount = discountService.getDiscountById(discountId);
                entities.add(order);
                entities.add(discount);
            }
        }
        // Handle PUT methods
        else if ("PUT".equalsIgnoreCase(method)) {
            // Pattern to match /bills/settle/{billId}
            if (path.matches("/bills/settle/\\d+")) {
                Long billId = extractIdFromPath(path, 3);
                Bill bill = billService.getBillById(billId);
                if (bill != null) {
                    entities.add(bill);
                }
            }
        }
        // Handle DELETE methods
        else if ("DELETE".equalsIgnoreCase(method)) {
            // Pattern to match /bills/{id}
            if (path.matches("/bills/\\d+")) {
                Long billId = extractIdFromPath(path, 2);
                Bill bill = billService.getBillById(billId);
                if (bill != null) {
                    entities.add(bill);
                }
            }
        }

        // Return the list of entities (could be empty)
        return entities;
    }



    private List<CompanyAssociatedEntity> resolveCustomerEntity(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        List<CompanyAssociatedEntity> entities = new ArrayList<>();

        // Handle GET methods
        if ("GET".equalsIgnoreCase(method)) {
            // Pattern to match /customers/{id}
            if (path.matches("/customers/\\d+")) {
                Long customerId = extractIdFromPath(path, 2);
                Customer customer = customerService.getCustomer(customerId);
                if (customer != null) {
                    entities.add(customer);
                }
            }
        }
        // Handle PUT methods
        else if ("PUT".equalsIgnoreCase(method)) {
            // Pattern to match /customers/{id}
            if (path.matches("/customers/\\d+")) {
                Long customerId = extractIdFromPath(path, 2);
                Customer customer = customerService.getCustomer(customerId);
                if (customer != null) {
                    entities.add(customer);
                }
            }
        }
        // Handle DELETE methods
        else if ("DELETE".equalsIgnoreCase(method)) {
            // Pattern to match /customers/{id}
            if (path.matches("/customers/\\d+")) {
                Long customerId = extractIdFromPath(path, 2);
                Customer customer = customerService.getCustomer(customerId);
                if (customer != null) {
                    entities.add(customer);
                }
            }
        }

        // Return the list of entities (could be empty)
        return entities;
    }



    private List<CompanyAssociatedEntity> resolveDiscountEntity(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        List<CompanyAssociatedEntity> entities = new ArrayList<>();

        // Handle GET methods
        if ("GET".equalsIgnoreCase(method)) {
            // Pattern to match /discounts/{id}
            if (path.matches("/discounts/\\d+")) {
                Long discountId = extractIdFromPath(path, 2);
                Discount discount = discountService.getDiscountById(discountId);
                if (discount != null) {
                    entities.add(discount);
                }
            }
            // Pattern to match /discounts/calculateDiscountedTotal/{orderId}/{discountCode}
            else if (path.matches("/discounts/calculateDiscountedTotal/\\d+/\\d+")) {
                Long orderId = Long.parseLong(path.split("/")[3]);
                Long discountId =Long.parseLong(path.split("/")[4]);
                Order order = orderService.getOrderById(orderId);
                Discount discount = discountService.getDiscountById(discountId);
                entities.add(order);
                entities.add(discount);
            }
        }
        // Handle PUT methods
        else if ("PUT".equalsIgnoreCase(method)) {
            // Pattern to match /discounts/{id}
            if (path.matches("/discounts/\\d+")) {
                Long discountId = extractIdFromPath(path, 2);
                Discount discount = discountService.getDiscountById(discountId);
                if (discount != null) {
                    entities.add(discount);
                }
            }
        }
        // Handle DELETE methods
        else if ("DELETE".equalsIgnoreCase(method)) {
            // Pattern to match /discounts/{id}
            if (path.matches("/discounts/\\d+")) {
                Long discountId = extractIdFromPath(path, 2);
                Discount discount = discountService.getDiscountById(discountId);
                if (discount != null) {
                    entities.add(discount);
                }
            }
        }

        // Return the list of entities (could be empty)
        return entities;
    }



    private List<CompanyAssociatedEntity> resolveEmployeeEntity(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        List<CompanyAssociatedEntity> entities = new ArrayList<>();

        // Handle GET methods
        if ("GET".equalsIgnoreCase(method)) {
            // Pattern to match /employees/{id}
            if (path.matches("/employees/\\d+")) {
                Long employeeId = extractIdFromPath(path, 2);
                Employee employee = employeeService.getEmployeeById(employeeId);
                if (employee != null) {
                    entities.add(employee);
                }
            }
        }
        // Handle PUT methods
        else if ("PUT".equalsIgnoreCase(method)) {
            // Pattern to match /employees/{id}
            if (path.matches("/employees/\\d+")) {
                Long employeeId = extractIdFromPath(path, 2);
                Employee employee = employeeService.getEmployeeById(employeeId);
                if (employee != null) {
                    entities.add(employee);
                }
            }
        }
        // Handle DELETE methods
        else if ("DELETE".equalsIgnoreCase(method)) {
            // Pattern to match /employees/{id}
            if (path.matches("/employees/\\d+")) {
                Long employeeId = extractIdFromPath(path, 2);
                Employee employee = employeeService.getEmployeeById(employeeId);
                if (employee != null) {
                    entities.add(employee);
                }
            }
        }

        // Return the list of entities (could be empty)
        return entities;
    }

    private List<CompanyAssociatedEntity> resolveFoodItemEntity(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        List<CompanyAssociatedEntity> entities = new ArrayList<>();

        // Handle GET methods
        if ("GET".equalsIgnoreCase(method)) {
            // Pattern to match /foodItems/id/{id}
            if (path.matches("/foodItems/id/\\d+")) {
                Long foodItemId = extractIdFromPath(path, 3);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                if (foodItem != null) {
                    entities.add(foodItem);
                }
            }
            // Pattern to match /foodItems/name/{name}
//            else if (path.startsWith("/foodItems/name/")) {
//                String foodItemName = path.split("/")[3];
//                FoodItem foodItem = foodItemService.findByItemName(foodItemName);
//                if (foodItem != null) {
//                    entities.add(foodItem);
//                }
//            }
        }
        // Handle PUT methods
        else if ("PUT".equalsIgnoreCase(method)) {
            // Pattern to match /foodItems/{id}
            if (path.matches("/foodItems/\\d+")) {
                Long foodItemId = extractIdFromPath(path, 2);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                if (foodItem != null) {
                    entities.add(foodItem);
                }
            }
        }
        // Handle DELETE methods
        else if ("DELETE".equalsIgnoreCase(method)) {
            // Pattern to match /foodItems/{id}
            if (path.matches("/foodItems/\\d+")) {
                Long foodItemId = extractIdFromPath(path, 2);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                if (foodItem != null) {
                    entities.add(foodItem);
                }
            }
        }

        // Return the list of entities (could be empty)
        return entities;
    }


    private List<CompanyAssociatedEntity> resolveInventoryEntity(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        List<CompanyAssociatedEntity> entities = new ArrayList<>();

        // Handle GET methods
        if ("GET".equalsIgnoreCase(method)) {
            // Pattern to match /inventories/{id}
            if (path.matches("/inventories/\\d+")) {
                Long inventoryId = extractIdFromPath(path, 2);
                Inventory inventory = inventoryService.getInventoryById(inventoryId);
                if (inventory != null) {
                    entities.add(inventory);
                }
            }
        }
        // Handle PUT methods
        else if ("PUT".equalsIgnoreCase(method)) {
            // Pattern to match /inventories/{id}
            if (path.matches("/inventories/\\d+")) {
                Long inventoryId = extractIdFromPath(path, 2);
                Inventory inventory = inventoryService.getInventoryById(inventoryId);
                if (inventory != null) {
                    entities.add(inventory);
                }
            }
        }
        // Handle DELETE methods
        else if ("DELETE".equalsIgnoreCase(method)) {
            // Pattern to match /inventories/{id}
            if (path.matches("/inventories/\\d+")) {
                Long inventoryId = extractIdFromPath(path, 2);
                Inventory inventory = inventoryService.getInventoryById(inventoryId);
                if (inventory != null) {
                    entities.add(inventory);
                }
            }
        }

        // Return the list of entities (could be empty)
        return entities;
    }



    private List<CompanyAssociatedEntity> resolveOrderEntity(HttpServletRequest request) throws IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        List<CompanyAssociatedEntity> entities = new ArrayList<>();

        // Handle GET methods
        if ("GET".equalsIgnoreCase(method)) {
            // Pattern to match /orders/{id}
            if (path.matches("/orders/\\d+")) {
                Long orderId = extractIdFromPath(path, 2);
                Order order = orderService.getOrderById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }
            // Pattern to match /orders/duration/{orderId}
            else if (path.matches("/orders/duration/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Order order = orderService.findById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }

            // Pattern to match /foodItemOrders/{orderId}
            else if (path.matches("/orders/foodItemOrders/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Order order = orderService.getOrderById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }
            else if (path.matches("/orders/foodItemOrderById/\\d+")) {
                Long foodItemOrderId = extractIdFromPath(path, 3);
                FoodItemOrder foodItemOrder = orderService.getFoodItemOrderById(foodItemOrderId);
                if (foodItemOrder != null) {
                    entities.add(foodItemOrder);
                }
            }
            // Pattern to match /foodItemOrder/duration/{orderId}/{foodItemId}
           else if (path.matches("/orders/foodItemOrder/duration/\\d+/\\d+")) {
                Long orderId = extractIdFromPath(path, 4);
                Long foodItemId = extractIdFromPath(path, 5);
                Order order = orderService.getOrderById(orderId);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                if (order != null) {
                    entities.add(order);
                }
                if (foodItem != null) {
                    entities.add(foodItem);
                }
            }

            // Pattern to match /foodItemOrderDetails/{orderId}
            else if (path.matches("/orders/foodItemOrderDetails/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Order order = orderService.getOrderById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }

            // Pattern to match /foodItemOrderDetail/duration/{orderId}/{foodItemId}/{foodItemOrderDetailId}
            else if (path.matches("/orders/foodItemOrderDetail/duration/\\d+/\\d+/\\d+")) {
                Long orderId = extractIdFromPath(path, 4);
                Long foodItemId = extractIdFromPath(path, 5);
                Long foodItemOrderDetailId = extractIdFromPath(path, 6);
                Order order = orderService.getOrderById(orderId);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                FoodItemOrderDetail foodItemOrderDetail = orderService.getFoodItemOrderDetailById(foodItemOrderDetailId);
                if (order != null) {
                    entities.add(order);
                }
                if (foodItem != null) {
                    entities.add(foodItem);
                }
                if (foodItemOrderDetail != null) {
                    entities.add(foodItemOrderDetail);
                }
            }

        }
        // Handle POST methods
        else if ("POST".equalsIgnoreCase(method)) {
            // Pattern to match /orders/{orderId}
            if (path.matches("/orders/\\d+")) {
                Long orderId = extractIdFromPath(path, 2);
                Order order = orderService.findById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }
            // Pattern to match /orders/saveWithEmployee/{orderId}/{employeeId}
            else if (path.matches("/orders/saveWithEmployee/\\d+/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Long employeeId = extractIdFromPath(path, 4);
                Order order = orderService.findById(orderId);
                Employee employee = employeeService.getEmployeeById(employeeId);
                if (order != null) {
                    entities.add(order);
                }
                if (employee != null) {
                    entities.add(employee);
                }
            }
        }
        // Handle PUT methods
        else if ("PUT".equalsIgnoreCase(method)) {
            // Pattern to match /orders/addFoodItem/{orderId}/{foodItemId}/{quantity}
            if (path.matches("/orders/addFoodItem/\\d+/\\d+/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Long foodItemId = extractIdFromPath(path, 4);
                Order order = orderService.getOrderById(orderId);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                if (order != null) {
                    entities.add(order);
                }
                if (foodItem != null) {
                    entities.add(foodItem);
                }
            }
            // Pattern to match /orders/removeFoodItem/{orderId}/{foodItemId}/{quantity}
            else if (path.matches("/orders/removeFoodItem/\\d+/\\d+/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Long foodItemId = extractIdFromPath(path, 4);
                Order order = orderService.getOrderById(orderId);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                if (order != null) {
                    entities.add(order);
                }
                if (foodItem != null) {
                    entities.add(foodItem);
                }
            }
            // Pattern to match /orders/comment/{orderId}
            else if (path.matches("/orders/comment/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Order order = orderService.getOrderById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }
            // Pattern to match /orders/comment/{orderId}/{foodItemId}
            else if (path.matches("/orders/comment/\\d+/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Long foodItemId = extractIdFromPath(path, 4);
                Order order = orderService.getOrderById(orderId);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                if (order != null) {
                    entities.add(order);
                }
                if (foodItem != null) {
                    entities.add(foodItem);
                }
            }
            // Pattern to match /orders/status/{orderId}
            else if (path.matches("/orders/status/\\d+")) {
                Long orderId = extractIdFromPath(path, 3);
                Order order = orderService.getOrderById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }

            // Pattern to match /foodItemOrder/status/{orderId}/{foodItemId}
            if (path.matches("/orders/foodItemOrder/status/\\d+/\\d+/\\d+")) {
                Long orderId = extractIdFromPath(path, 4);
                Long foodItemId = extractIdFromPath(path, 5);
                Long foodItemOrderDetailId = extractIdFromPath(path, 6);
                Order order = orderService.getOrderById(orderId);
                FoodItem foodItem = foodItemService.getFoodItemById(foodItemId);
                FoodItemOrderDetail foodItemOrderDetail = orderService.getFoodItemOrderDetailById(foodItemOrderDetailId);
                if (order != null) {
                    entities.add(order);
                }
                if (foodItem != null) {
                    entities.add(foodItem);
                }
                if (foodItemOrderDetail != null) {
                    entities.add(foodItemOrderDetail);
                }
            }
        }
        // Handle DELETE methods
        else if ("DELETE".equalsIgnoreCase(method)) {
            // Pattern to match /orders/{id}
            if (path.matches("/orders/\\d+")) {
                Long orderId = extractIdFromPath(path, 2);
                Order order = orderService.findById(orderId);
                if (order != null) {
                    entities.add(order);
                }
            }
        }

        // Return the list of entities (could be empty)
        return entities;
    }



    private List<CompanyAssociatedEntity> resolveTableEntity(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        List<CompanyAssociatedEntity> entities = new ArrayList<>();

        // Handle GET methods
        if ("GET".equalsIgnoreCase(method)) {
            // Pattern to match /tables/{tableId}
            if (path.matches("/tables/\\d+")) {
                Long tableId = extractIdFromPath(path, 2);
                RestaurantTable table = tableService.getTableById(tableId);
                if (table != null) {
                    entities.add(table);
                }
            }
            // Pattern to match /tables/status/{tableId}
            else if (path.matches("/tables/status/\\d+")) {
                Long tableId = extractIdFromPath(path, 3);
                RestaurantTable table = tableService.getTableById(tableId);
                if (table != null) {
                    entities.add(table);
                }
            }
        }
        // Handle POST methods
        else if ("POST".equalsIgnoreCase(method)) {
            // Pattern to match /tables/createOrderForTable/{tableId}
            if (path.matches("/tables/createOrderForTable/\\d+")) {
                Long tableId = extractIdFromPath(path, 3);
                RestaurantTable table = tableService.getTableById(tableId);
                if (table != null) {
                    entities.add(table);
                }
            }
        }
        // Handle PUT methods
        else if ("PUT".equalsIgnoreCase(method)) {
            // Pattern to match /tables/{tableId}
            if (path.matches("/tables/\\d+")) {
                Long tableId = extractIdFromPath(path, 2);
                RestaurantTable table = tableService.getTableById(tableId);
                if (table != null) {
                    entities.add(table);
                }
            }
            // Pattern to match /tables/occupy/{tableId}
            else if (path.matches("/tables/occupy/\\d+")) {
                Long tableId = extractIdFromPath(path, 3);
                RestaurantTable table = tableService.getTableById(tableId);
                if (table != null) {
                    entities.add(table);
                }
            }
            // Pattern to match /tables/complete/{tableId}
            else if (path.matches("/tables/complete/\\d+")) {
                Long tableId = extractIdFromPath(path, 3);
                RestaurantTable table = tableService.getTableById(tableId);
                if (table != null) {
                    entities.add(table);
                }
            }
            // Pattern to match /tables/free/{tableId}
            else if (path.matches("/tables/free/\\d+")) {
                Long tableId = extractIdFromPath(path, 3);
                RestaurantTable table = tableService.getTableById(tableId);
                if (table != null) {
                    entities.add(table);
                }
            }
        }
        // Handle DELETE methods
        else if ("DELETE".equalsIgnoreCase(method)) {
            // Pattern to match /tables/{tableId}
            if (path.matches("/tables/\\d+")) {
                Long tableId = extractIdFromPath(path, 2);
                RestaurantTable table = tableService.getTableById(tableId);
                if (table != null) {
                    entities.add(table);
                }
            }
        }

        // Return the list of entities (could be empty)
        return entities;
    }



    private Long extractIdFromPath(String path, int position) {
        String[] pathParts = path.split("/");
        if (pathParts.length > position) {
            return Long.parseLong(pathParts[position]);
        }
        return null;
    }
}



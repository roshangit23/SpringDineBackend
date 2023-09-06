package com.hotelPro.hotelmanagementsystem.filter;

import com.hotelPro.hotelmanagementsystem.controller.DTO.FoodItemOrderDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.MergeOrdersRequestDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.OrderRequestDTO;
import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.model.Customer;
import com.hotelPro.hotelmanagementsystem.model.Employee;
import com.hotelPro.hotelmanagementsystem.model.FoodItem;
import com.hotelPro.hotelmanagementsystem.model.RestaurantTable;
import com.hotelPro.hotelmanagementsystem.service.CustomerService;
import com.hotelPro.hotelmanagementsystem.service.EmployeeService;
import com.hotelPro.hotelmanagementsystem.service.FoodItemService;
import com.hotelPro.hotelmanagementsystem.service.RestaurantTableService;
import com.hotelPro.hotelmanagementsystem.util.JwtTokenProvider;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ValidationAspect {

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RestaurantTableService restaurantTableService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Before("@annotation(com.hotelPro.hotelmanagementsystem.filter.ValidateOrderRequest) && args(orderRequest,..)")
    public void validateOrder(JoinPoint joinPoint, OrderRequestDTO orderRequest) {
        //Extract companyId from the token
        Long companyIdFromToken = getCompanyIdFromToken();

        if (orderRequest.getFoodItemOrders() != null || !orderRequest.getFoodItemOrders().isEmpty()) {
            for (FoodItemOrderDTO foodItemOrderDTO : orderRequest.getFoodItemOrders()) {
                if (foodItemOrderDTO != null && foodItemOrderDTO.getFoodItemId() != null) {
                    FoodItem foodItem = foodItemService.getFoodItemById(foodItemOrderDTO.getFoodItemId());
                    if (foodItem != null && !foodItem.getCompany().getId().equals(companyIdFromToken)) {
                        throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
                    }
                }
            }
        }

        if (orderRequest.getCustomerId() != null) {
            Customer customer = customerService.getCustomer(orderRequest.getCustomerId());
            if (customer != null && !customer.getCompany().getId().equals(companyIdFromToken)) {
                throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
            }
        }

         if (orderRequest.getEmployeeId() != null) {
            Employee employee = employeeService.getEmployeeById(orderRequest.getEmployeeId());
            if (employee != null && !employee.getCompany().getId().equals(companyIdFromToken)) {
                throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
            }
        }

    }

    @Before("@annotation(com.hotelPro.hotelmanagementsystem.filter.ValidateMergeOrdersRequest) && args(mergeOrdersRequest,..)")
    public void validateMergeOrders(JoinPoint joinPoint, MergeOrdersRequestDTO mergeOrdersRequest) {
        Long companyIdFromToken = getCompanyIdFromToken();
        if (mergeOrdersRequest.getTableIds() == null || mergeOrdersRequest.getTableIds().isEmpty()) {
            throw new CustomException("Table IDs list is empty or null", HttpStatus.BAD_REQUEST);
        }
        for (Long tableId : mergeOrdersRequest.getTableIds()) {
            RestaurantTable restaurantTable = restaurantTableService.getTableById(tableId);
            if (restaurantTable != null && !restaurantTable.getCompany().getId().equals(companyIdFromToken)) {
                throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
            }
        }
    }
    private Long getCompanyIdFromToken() {
        // Extract the token from the request header
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }
        token = token.substring(7); // strip off "Bearer "
        // Decode the token and extract the companyId
        Long companyId = jwtTokenProvider.getClaimFromToken(token, "companyId", Long.class);

        return companyId;
    }
}

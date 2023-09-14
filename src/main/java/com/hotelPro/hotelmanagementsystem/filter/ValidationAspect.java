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

import java.util.Collections;
import java.util.List;

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
        // Extract companyIds based on user role
        List<Long> companyIdsForUser = getCompanyIdsForUser();
        if (orderRequest.getFoodItemOrders() != null || !orderRequest.getFoodItemOrders().isEmpty()) {
            for (FoodItemOrderDTO foodItemOrderDTO : orderRequest.getFoodItemOrders()) {
                if (foodItemOrderDTO != null && foodItemOrderDTO.getFoodItemId() != null) {
                    FoodItem foodItem = foodItemService.getFoodItemById(foodItemOrderDTO.getFoodItemId());
                    if (foodItem != null && !companyIdsForUser.contains(foodItem.getCompany().getId())) {
                        throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
                    }
                }
            }
        }

        if (orderRequest.getCustomerId() != null) {
            Customer customer = customerService.getCustomer(orderRequest.getCustomerId());
            if (customer != null &&   !companyIdsForUser.contains(customer.getCompany().getId())) {
                throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
            }
        }

         if (orderRequest.getEmployeeId() != null) {
            Employee employee = employeeService.getEmployeeById(orderRequest.getEmployeeId());
            if (employee != null && !companyIdsForUser.contains(employee.getCompany().getId())) {
                throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
            }
        }

    }

    @Before("@annotation(com.hotelPro.hotelmanagementsystem.filter.ValidateMergeOrdersRequest) && args(mergeOrdersRequest,..)")
    public void validateMergeOrders(JoinPoint joinPoint, MergeOrdersRequestDTO mergeOrdersRequest) {
        // Extract companyIds based on user role
        List<Long> companyIdsForUser = getCompanyIdsForUser();
        if (mergeOrdersRequest.getTableIds() == null || mergeOrdersRequest.getTableIds().isEmpty()) {
            throw new CustomException("Table IDs list is empty or null", HttpStatus.BAD_REQUEST);
        }
        for (Long tableId : mergeOrdersRequest.getTableIds()) {
            RestaurantTable restaurantTable = restaurantTableService.getTableById(tableId);
            if (restaurantTable != null && !companyIdsForUser.contains(restaurantTable.getCompany().getId())) {
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

    private List<Long> getCompanyIdsForUser() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }
        token = token.substring(7); // strip off "Bearer "

        List<String> roles = jwtTokenProvider.getClaimFromToken(token, "roles", List.class);
        if (roles.contains("ROLE_DASHBOARD_USER")) {
            return jwtTokenProvider.getClaimFromToken(token, "companyIds", List.class);
        } else {
            return Collections.singletonList(getCompanyIdFromToken());
        }
    }
}

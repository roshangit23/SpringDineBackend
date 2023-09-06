package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Customer;
import com.hotelPro.hotelmanagementsystem.model.Order;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer, Long companyId);
    Customer saveCustomerForOrder(Customer customer, Order.OrderType orderType);
    Customer saveCustomerForBill(Customer customer, Bill.PaymentMode paymentMode);
    Customer getCustomer(Long id);
    Customer updateCustomer(Long id, Customer customerDetails);
    void deleteCustomer(Long id);

    List<Customer> getAllCustomers(Long companyId);
}

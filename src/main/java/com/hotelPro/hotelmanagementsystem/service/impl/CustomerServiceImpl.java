package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.Customer;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.CustomerRepository;
import com.hotelPro.hotelmanagementsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CompanyRepository companyRepository;
   @Override
    public Customer createCustomer(Customer customer,Long companyId) {
       Company company = companyRepository.findById(companyId)
               .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        customer.setCompany(company);
       // Generate unique orderNo for the order within the company
       Long lastCustomerNo = customerRepository.findMaxCustomerNoByCompany(company.getId());
       customer.setCustomerNo(lastCustomerNo == null ? 1 : lastCustomerNo + 1);
        return customerRepository.save(customer);
    }
    @Override
    public Customer saveCustomerForOrder(Customer customer, Order.OrderType orderType,Long companyId) {
        // Validate that the name is not blank
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new CustomException("Name is mandatory", HttpStatus.BAD_REQUEST);
        }
        // Check if a customer with this phone number already exists
        Optional<Customer> existingCustomer = Optional.ofNullable(customerRepository.findByPhoneNumber(customer.getPhoneNumber()));
        // If the order type is delivery, validate that the  phone number and address is not blank
        if (orderType == Order.OrderType.DELIVERY) {
            if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty()) {
                throw new CustomException("Phone number is mandatory for delivery orders", HttpStatus.BAD_REQUEST);
            }
            if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                throw new CustomException("Address is mandatory for delivery orders", HttpStatus.BAD_REQUEST);
            }

            if (existingCustomer.isPresent()) {
                throw new CustomException("A customer with this phone number already exists", HttpStatus.BAD_REQUEST);
            }
        }
        if (existingCustomer.isEmpty()) {
            // Generate unique orderNo for the order within the company
            Long lastCustomerNo = customerRepository.findMaxCustomerNoByCompany(companyId);
            customer.setCustomerNo(lastCustomerNo == null ? 1 : lastCustomerNo + 1);
        }
        // Save the customer to the database
        return customerRepository.save(customer);
    }
    @Override
    public Customer saveCustomerForBill(Customer customer, Bill.PaymentMode paymentMode,Long companyId) {
        if (paymentMode == Bill.PaymentMode.DUE) {
            // Validate that the phone number is not blank
            if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty()) {
                throw new CustomException("Phone number is mandatory for due orders", HttpStatus.BAD_REQUEST);
            }
           Customer existingCustomer = customerRepository.findByPhoneNumber(customer.getPhoneNumber());
            if(existingCustomer!=null){
                if (existingCustomer.getName() == null || existingCustomer.getName().trim().isEmpty()) {
                    throw new CustomException("Name is mandatory for due orders", HttpStatus.BAD_REQUEST);
                }
                if (existingCustomer.getAddress() == null || existingCustomer.getAddress().trim().isEmpty()) {
                        throw new CustomException("Address is mandatory for due orders", HttpStatus.BAD_REQUEST);
                    }
                return customerRepository.save(existingCustomer);
            }
           else {
                // Validate that the name is not blank
                if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                    throw new CustomException("Name is mandatory", HttpStatus.BAD_REQUEST);
                }

                if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
                    throw new CustomException("Address is mandatory for delivery orders", HttpStatus.BAD_REQUEST);
                }
                // Generate unique orderNo for the order within the company
                Long lastCustomerNo = customerRepository.findMaxCustomerNoByCompany(companyId);
                customer.setCustomerNo(lastCustomerNo == null ? 1 : lastCustomerNo + 1);
                return customerRepository.save(customer);
            }
        }

        // Save the customer to the database
        return customerRepository.save(customer);
    }
    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
    }
    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomer(id);
        if (customerDetails.getName() != null && !customerDetails.getName().isEmpty()) {
            customer.setName(customerDetails.getName());
        }
        if (customerDetails.getPhoneNumber() != null && !customerDetails.getPhoneNumber().isEmpty()) {
            customer.setPhoneNumber(customerDetails.getPhoneNumber());
        }
        if (customerDetails.getEmail() != null && !customerDetails.getEmail().isEmpty()) {
            customer.setEmail(customerDetails.getEmail());
        }
        if (customerDetails.getAddress() != null && !customerDetails.getAddress().isEmpty()) {
            customer.setAddress(customerDetails.getAddress());
        }
        return customerRepository.save(customer);
    }
    @Override
    public void deleteCustomer(Long id) {
        Customer customer = getCustomer(id);
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getAllCustomers(Long companyId) {
        return customerRepository.findByCompanyId(companyId);
       //return customerRepository.findAll();
    }
    @Override
    public Customer findByCustomerNoAndCompanyId(Long customerNo, Long companyId){
        return customerRepository.findByCustomerNoAndCompanyId(customerNo, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "Customer No", customerNo));
    }

    @Override
    public Customer findByPhoneNumberAndCompanyId(String phoneNumber, Long companyId) {
        return customerRepository.findByPhoneNumberAndCompanyId(phoneNumber, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "Phone Number", phoneNumber));
    }

}

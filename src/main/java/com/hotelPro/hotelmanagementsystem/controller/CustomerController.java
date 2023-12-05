package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.CustomerResponseDTO;
import com.hotelPro.hotelmanagementsystem.model.Customer;
import com.hotelPro.hotelmanagementsystem.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/createCustomer/{companyId}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@Valid @RequestBody Customer customer, @PathVariable Long companyId) {
        Customer createdCustomer = customerService.createCustomer(customer, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), new CustomerResponseDTO(createdCustomer)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomer(@PathVariable Long id) {
        Customer customer = customerService.getCustomer(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new CustomerResponseDTO(customer)));
    }

    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getAllCustomers(@PathVariable Long companyId) {
        List<Customer> customers = customerService.getAllCustomers(companyId);
        List<CustomerResponseDTO> customerResponseDTOs = customers.stream()
                .map(CustomerResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), customerResponseDTOs));
    }
    @GetMapping("/getByCustomerNo/{companyId}/{customerNo}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerByCustomerNoAndCompanyId(@PathVariable Long companyId, @PathVariable Long customerNo) {
        Customer customer = customerService.findByCustomerNoAndCompanyId(customerNo, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new CustomerResponseDTO(customer)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new CustomerResponseDTO(updatedCustomer)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Customer deleted successfully"));
    }

    @GetMapping("/getByPhoneNumber/{companyId}/{phoneNumber}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerByPhoneNumberAndCompanyId(@PathVariable Long companyId, @PathVariable String phoneNumber) {
        Customer customer = customerService.findByPhoneNumberAndCompanyId(phoneNumber, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new CustomerResponseDTO(customer)));
    }

}


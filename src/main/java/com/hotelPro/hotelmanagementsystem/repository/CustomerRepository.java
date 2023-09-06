package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPhoneNumber(String phoneNumber);

    List<Customer> findByCompanyId(Long companyId);
}

package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPhoneNumber(String phoneNumber);

    List<Customer> findByCompanyId(Long companyId);
    @Query("SELECT MAX(c.customerNo) FROM Customer c WHERE c.company.id = :companyId")
    Long findMaxCustomerNoByCompany(@Param("companyId") Long companyId);

    Optional<Customer> findByCustomerNoAndCompanyId(Long customerNo, Long companyId);

    Optional<Customer> findByPhoneNumberAndCompanyId(String phoneNumber, Long companyId);
}

package com.hotelPro.hotelmanagementsystem.repository;

import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository  extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByCompanyId(Long companyId);

    Optional<Subscription> findByNameAndCompany(Subscription.SubscriptionName subscriptionName, Company company);
}

package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.controller.DTO.SubscriptionRequestDTO;
import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.InvalidEnumValueException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.Subscription;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.SubscriptionRepository;
import com.hotelPro.hotelmanagementsystem.service.SubscriptionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Subscription createSubscription(SubscriptionRequestDTO subscriptionDTO, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

        // Parse the subscription name from the request
        Subscription.SubscriptionName subscriptionName;
        try {
            subscriptionName = Subscription.SubscriptionName.valueOf(subscriptionDTO.getName());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException("name", "Invalid value for name");
        }

        // Check if a subscription with the same name already exists for the company
        Optional<Subscription> existingSubscription = subscriptionRepository.findByCompanyId(company.getId());
        if (existingSubscription.isPresent()) {
            throw new CustomException("Already subscribed to a plan", HttpStatus.CONFLICT);
        }

        Subscription subscription = new Subscription();
        subscription.setName(subscriptionName);
        subscription.setDescription(subscriptionDTO.getDescription());
        subscription.setPrice(subscriptionDTO.getPrice());
        subscription.setCompany(company);

        // Set expiry date based on duration
        LocalDate expiryDate = calculateExpiryDate(subscriptionDTO.getDuration());
        subscription.setExpiryDate(expiryDate);

        return subscriptionRepository.save(subscription);
    }


    private LocalDate calculateExpiryDate(String duration) {
        LocalDate now = LocalDate.now();
        switch (duration) {
            case "3m":
                return now.plusMonths(3);
            case "6m":
                return now.plusMonths(6);
            case "1y":
                return now.plusYears(1);
            case "2y":
                return now.plusYears(2);
            case "3y":
                return now.plusYears(3);
            default:
                throw new IllegalArgumentException("Invalid duration: " + duration);
        }
    }

    @Override
    public void removeSubscription(Long companyId, Long subscriptionId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", subscriptionId));

        // Ensure the subscription belongs to the specified company
        if (!subscription.getCompany().getId().equals(company.getId())) {
            throw new CustomException("The subscription does not belong to the specified company", HttpStatus.FORBIDDEN);
        }

        // Disconnect the relationship from both sides
        //subscription.setCompany(null);
        company.setSubscription(null);

        // Save the company to ensure the relationship is updated
        companyRepository.save(company);

        // Now delete the subscription
        subscriptionRepository.delete(subscription);
    }


    @Override
    @Transactional
    public Subscription getSubscriptionByCompanyId(Long companyId) {
        return subscriptionRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "Company id", companyId));
    }
}

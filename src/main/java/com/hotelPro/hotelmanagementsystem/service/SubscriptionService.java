package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.controller.DTO.SubscriptionRequestDTO;
import com.hotelPro.hotelmanagementsystem.model.Subscription;

public interface SubscriptionService {
    Subscription createSubscription(SubscriptionRequestDTO subscriptionDTO, Long companyId);
    void removeSubscription(Long companyId, Long subscriptionId);
    Subscription getSubscriptionByCompanyId(Long companyId);
}

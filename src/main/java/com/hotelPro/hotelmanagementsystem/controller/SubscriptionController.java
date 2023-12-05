package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.SubscriptionRequestDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.SubscriptionResponseDTO;
import com.hotelPro.hotelmanagementsystem.model.Subscription;
import com.hotelPro.hotelmanagementsystem.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/createSubscription/{companyId}")
    public ResponseEntity<ApiResponse<SubscriptionResponseDTO>> createSubscription(
            @Valid @RequestBody SubscriptionRequestDTO subscriptionDTO,
            @PathVariable Long companyId) {
        Subscription createdSubscription = subscriptionService.createSubscription(subscriptionDTO, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), new SubscriptionResponseDTO(createdSubscription)));
    }

    @DeleteMapping("/removeSubscription/{companyId}/{subscriptionId}")
    public ResponseEntity<ApiResponse<String>> removeSubscription(@PathVariable Long companyId,@PathVariable Long subscriptionId) {
        subscriptionService.removeSubscription(companyId,subscriptionId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Subscription removed successfully"));
    }

    @GetMapping("/getSubscriptionDetails/{companyId}")
    public ResponseEntity<ApiResponse<SubscriptionResponseDTO>> getSubscriptionDetails(@PathVariable Long companyId) {
        Subscription subscription = subscriptionService.getSubscriptionByCompanyId(companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),  new SubscriptionResponseDTO(subscription)));
    }
}

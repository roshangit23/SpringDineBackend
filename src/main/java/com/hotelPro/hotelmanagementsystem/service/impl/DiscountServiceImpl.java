package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.controller.DTO.DiscountRequestDTO;
import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.InvalidEnumValueException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.DiscountRepository;
import com.hotelPro.hotelmanagementsystem.service.DiscountService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private CompanyRepository companyRepository;

    private Discount convertToEntity(DiscountRequestDTO discountDTO) {
        Discount discount = new Discount();
        discount.setDiscountCode(discountDTO.getDiscountCode());
        discount.setPercentage(discountDTO.getPercentage());

        // Handle applicable order types
        Set<String> applicableOrderTypesStr = discountDTO.getApplicableOrderTypes();
        if (applicableOrderTypesStr != null && !applicableOrderTypesStr.isEmpty()) {
            Set<Order.OrderType> orderTypes = applicableOrderTypesStr.stream()
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .map(orderTypeStr -> {
                        try {
                            return Order.OrderType.valueOf(orderTypeStr.replace(" ", "_"));
                        } catch (IllegalArgumentException e) {
                            throw new InvalidEnumValueException("applicableOrderType", "Invalid value for applicableOrderType: " + orderTypeStr);
                        }
                    })
                    .collect(Collectors.toSet());
            discount.setApplicableOrderType(orderTypes);  // Assuming you have updated your Discount entity accordingly
        }

        discount.setMinimumBillAmount(discountDTO.getMinimumBillAmount());

        // Handle applicable payment modes
        Set<String> applicablePaymentModesStr = discountDTO.getApplicablePaymentModes();
        if (applicablePaymentModesStr != null && !applicablePaymentModesStr.isEmpty()) {
            Set<Bill.PaymentMode> paymentModes = applicablePaymentModesStr.stream()
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .map(paymentModeStr -> {
                        try {
                            return Bill.PaymentMode.valueOf(paymentModeStr.replace(" ", "_"));
                        } catch (IllegalArgumentException e) {
                            throw new InvalidEnumValueException("applicablePaymentMode", "Invalid value for applicablePaymentMode: " + paymentModeStr);
                        }
                    })
                    .collect(Collectors.toSet());
            discount.setApplicablePaymentMode(paymentModes);  // Assuming you have updated your Discount entity accordingly
        }

        // ... other code ...

        return discount;
    }
    @Override
    public Discount createDiscount(DiscountRequestDTO discountDTO,Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        Discount discount = convertToEntity(discountDTO);
        discount.setCompany(company);
        return discountRepository.save(discount);
    }

    @Override
    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", "id", id));
    }
    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Enum.valueOf(enumClass, value.trim().toUpperCase());
    }

    @Override
    @Transactional
    public Discount findByDiscountCodeAndCompanyId(String code, Long companyId) {
        // Update the method to also search by companyId
        return discountRepository.findByDiscountCodeAndCompanyId(code, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", "Discount code", code));
    }

    @Override
    public Discount updateDiscount(Long id, DiscountRequestDTO discountDetails) {
        Discount discount = getDiscountById(id);
        // Update the fields of the existing discount as needed
        if (discountDetails.getDiscountCode() != null && !discountDetails.getDiscountCode().isEmpty()) {
            discount.setDiscountCode(discountDetails.getDiscountCode());
        }
        if (discountDetails.getPercentage()!=null && discountDetails.getPercentage() >= 0.0) {
            discount.setPercentage(discountDetails.getPercentage());
        }
        // Handle applicable order types
        Set<String> applicableOrderTypeStrs = discountDetails.getApplicableOrderTypes();
        if (applicableOrderTypeStrs != null && !applicableOrderTypeStrs.isEmpty()) {
            Set<Order.OrderType> applicableOrderTypes = new HashSet<>();
            for (String orderTypeStr : applicableOrderTypeStrs) {
                try {
                    Order.OrderType orderType = Order.OrderType.valueOf(orderTypeStr.trim().toUpperCase());
                    applicableOrderTypes.add(orderType);
                } catch (IllegalArgumentException e) {
                    throw new InvalidEnumValueException("applicableOrderType", "Invalid value for applicableOrderType");
                }
            }
            discount.setApplicableOrderType(applicableOrderTypes);
        }
        if (discountDetails.getMinimumBillAmount()!=null && discountDetails.getMinimumBillAmount() >= 0.0) {
            discount.setMinimumBillAmount(discountDetails.getMinimumBillAmount());
        }
        // Handle applicable payment modes
        Set<String> applicablePaymentModeStrs = discountDetails.getApplicablePaymentModes();
        if (applicablePaymentModeStrs != null && !applicablePaymentModeStrs.isEmpty()) {
            Set<Bill.PaymentMode> applicablePaymentModes = new HashSet<>();
            for (String paymentModeStr : applicablePaymentModeStrs) {
                try {
                    Bill.PaymentMode paymentMode = Bill.PaymentMode.valueOf(paymentModeStr.trim().toUpperCase());
                    applicablePaymentModes.add(paymentMode);
                } catch (IllegalArgumentException e) {
                    throw new InvalidEnumValueException("applicablePaymentMode", "Invalid value for applicablePaymentMode");
                }
            }
            discount.setApplicablePaymentMode(applicablePaymentModes);
        }
        return discountRepository.save(discount);
    }

    @Override
    public void deleteDiscount(Long id) {
        Discount discount = getDiscountById(id);
        discountRepository.delete(discount);
    }

    @Override
    public double calculateDiscountedTotal(Order order, Discount discount) {
        double total = orderService.calculateTotal(order);
        double discountAmount = orderService.calculateDiscount(order, discount);
        if (discountAmount <= 0) {
            throw new CustomException("The provided discount is not valid for this order", HttpStatus.BAD_REQUEST);
        }
        return total - discountAmount;
    }

    @Override
    public List<Discount> getAllDiscounts(Long companyId) {
        return discountRepository.findByCompanyId(companyId);
    }
}

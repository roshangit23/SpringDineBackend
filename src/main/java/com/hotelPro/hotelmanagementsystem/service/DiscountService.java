package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.controller.DTO.DiscountRequestDTO;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.Order;

import java.util.List;

public interface DiscountService {
    Discount createDiscount(DiscountRequestDTO discountDTO,Long companyId);
    Discount getDiscountById(Long id);
    Discount updateDiscount(Long id, DiscountRequestDTO discountDetails);
    void deleteDiscount(Long id);
    double calculateDiscountedTotal(Order order, Discount discount);
    List<Discount> getAllDiscounts(Long companyId);
    Discount findByDiscountCodeAndCompanyId(String code, Long companyId);
}

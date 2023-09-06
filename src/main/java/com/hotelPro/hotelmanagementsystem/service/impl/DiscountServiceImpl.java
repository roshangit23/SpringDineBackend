package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.controller.DTO.DiscountRequestDTO;
import com.hotelPro.hotelmanagementsystem.exception.InvalidEnumValueException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.DiscountRepository;
import com.hotelPro.hotelmanagementsystem.service.DiscountService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private CompanyRepository companyRepository;

private Discount convertToEntity(DiscountRequestDTO discountDTO) {
    // Here, you would map the fields from the DTO to a new Discount entity
    Discount discount = new Discount();
    discount.setDiscountCode(discountDTO.getDiscountCode());
    discount.setPercentage(discountDTO.getPercentage());

    if (discountDTO.getApplicableOrderType() != null && !discountDTO.getApplicableOrderType().trim().isEmpty()) {
        try {
            discount.setApplicableOrderType(Order.OrderType.valueOf(discountDTO.getApplicableOrderType()));
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException("applicableOrderType", "Invalid value for applicableOrderType");
        }
    }

        discount.setMinimumBillAmount(discountDTO.getMinimumBillAmount());

    if (discountDTO.getApplicablePaymentMode() != null && !discountDTO.getApplicablePaymentMode().trim().isEmpty()) {
        try {
            discount.setApplicablePaymentMode(Bill.PaymentMode.valueOf(discountDTO.getApplicablePaymentMode()));
        }
        catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException("applicablePaymentMode", "Invalid value for applicablePaymentMode");
        }
    }
    // Continue mapping other fields as necessary
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
    public Discount updateDiscount(Long id, DiscountRequestDTO discountDetails) {
        Discount discount = getDiscountById(id);
        // Update the fields of the existing discount as needed
        if (discountDetails.getDiscountCode() != null && !discountDetails.getDiscountCode().isEmpty()) {
            discount.setDiscountCode(discountDetails.getDiscountCode());
        }
        if (discountDetails.getPercentage()!=null && discountDetails.getPercentage() >= 0.0) {
            discount.setPercentage(discountDetails.getPercentage());
        }
//        if (discountDetails.getApplicableOrderType() != null) {
//           discount.setApplicableOrderType(discountDetails.getApplicableOrderType());
//       }
//        Order.OrderType applicableOrderType = parseEnum(Order.OrderType.class, discountDetails.getApplicableOrderType());
//        if (applicableOrderType != null) {
//            discount.setApplicableOrderType(applicableOrderType);
//        }
        // Handle applicable order type
        String applicableOrderTypeStr = discountDetails.getApplicableOrderType();
        if (applicableOrderTypeStr != null && !applicableOrderTypeStr.trim().isEmpty()) {
            try {
                Order.OrderType applicableOrderType = Order.OrderType.valueOf(applicableOrderTypeStr.trim().toUpperCase());
                discount.setApplicableOrderType(applicableOrderType);
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumValueException("applicableOrderType", "Invalid value for applicableOrderType");
            }
        }
        if (discountDetails.getMinimumBillAmount()!=null && discountDetails.getMinimumBillAmount()  >= 0.0) {
            discount.setMinimumBillAmount(discountDetails.getMinimumBillAmount());
        }
//        if (discountDetails.getApplicablePaymentMode() != null) {
//           discount.setApplicablePaymentMode(discountDetails.getApplicablePaymentMode());
//        }
//        Bill.PaymentMode applicablePaymentMode = parseEnum(Bill.PaymentMode.class, discountDetails.getApplicablePaymentMode());
//        if (applicablePaymentMode != null) {
//            discount.setApplicablePaymentMode(applicablePaymentMode);
//        }
        String applicablePaymentModeStr = discountDetails.getApplicablePaymentMode();
        if (applicablePaymentModeStr != null && !applicablePaymentModeStr.trim().isEmpty()) {
            try {
                Bill.PaymentMode applicablePaymentMode = Bill.PaymentMode.valueOf(applicablePaymentModeStr.trim().toUpperCase());
                discount.setApplicablePaymentMode(applicablePaymentMode);
            }
            catch (IllegalArgumentException e) {
                throw new InvalidEnumValueException("applicablePaymentMode", "Invalid value for applicablePaymentMode");
            }
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
            throw new IllegalArgumentException("The provided discount is not valid for this order");
        }
        return total - discountAmount;
    }
    @Override
    public Discount getDiscountByCode(String code) {
        return discountRepository.findBydiscountCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Discount", "code", code));
    }

    @Override
    public List<Discount> getAllDiscounts(Long companyId) {
        return discountRepository.findByCompanyId(companyId);
    //return discountRepository.findAll();
    }
}

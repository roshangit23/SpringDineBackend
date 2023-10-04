package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.DiscountRequestDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.DiscountResponseDTO;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.service.DiscountService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/createDiscount/{companyId}")
    public ResponseEntity<ApiResponse<DiscountResponseDTO>> createDiscount(@Valid @RequestBody DiscountRequestDTO discountDTO, @PathVariable Long companyId) {
        Discount createdDiscount = discountService.createDiscount(discountDTO, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), new DiscountResponseDTO(createdDiscount)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponseDTO>> getDiscountById(@PathVariable Long id) {
        Discount discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new DiscountResponseDTO(discount)));
    }

    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<DiscountResponseDTO>>> getAllDiscounts(@PathVariable Long companyId) {
        List<Discount> discounts = discountService.getAllDiscounts(companyId);
        List<DiscountResponseDTO> discountResponseDTOs = discounts.stream()
                .map(DiscountResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), discountResponseDTOs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscountResponseDTO>> updateDiscount(@PathVariable Long id, @RequestBody DiscountRequestDTO discountDTO) {
        Discount updatedDiscount = discountService.updateDiscount(id, discountDTO);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new DiscountResponseDTO(updatedDiscount)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Discount deleted successfully"));
    }

    @GetMapping("/calculateDiscountedTotal/{orderId}/{discountId}")
    public ResponseEntity<ApiResponse<Double>> calculateDiscountedTotal(@PathVariable Long orderId, @PathVariable Long discountId) {
        Order order = orderService.findById(orderId);
        Discount discount = discountService.getDiscountById(discountId);
        double discountedTotal = discountService.calculateDiscountedTotal(order, discount);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), discountedTotal));
    }
}



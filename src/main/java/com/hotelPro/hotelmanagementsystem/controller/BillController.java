package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.BillResponseDTO;
import com.hotelPro.hotelmanagementsystem.controller.DTO.DeleteBillRequest;
import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.service.BillService;
import com.hotelPro.hotelmanagementsystem.service.DiscountService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillService billService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DiscountService discountService;

    @PostMapping("/create/{orderId}")
    public ResponseEntity<ApiResponse<BillResponseDTO>> createBill(@PathVariable Long orderId,
                                                                   @RequestParam(required = false) Long discountId) {
        Order order = orderService.findById(orderId);

        Discount discount = null; // Initialize discount as null

        // Only fetch discount if a discountId is provided
        if(discountId != null) {
            discount = discountService.getDiscountById(discountId);
        }

        Bill bill = billService.saveBill(order, discount);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new BillResponseDTO(bill)));
    }

    @PutMapping("/settle/{billId}")
    public ResponseEntity<ApiResponse<BillResponseDTO>> settleBill(@PathVariable Long billId, @RequestBody Map<Bill.PaymentMode, Double> paymentModes) {
        Bill settledBill = billService.settleBill(billId, paymentModes);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new BillResponseDTO(settledBill)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponseDTO>> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new BillResponseDTO(bill)));
    }
    @GetMapping("/getByBillNo/{companyId}/{billNo}")
    public ResponseEntity<ApiResponse<BillResponseDTO>> getByBillNoAndCompanyId(@PathVariable Long companyId, @PathVariable Long billNo) {
        Bill bill = billService.findByBillNoAndCompanyId(billNo, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new BillResponseDTO(bill)));
    }
    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<BillResponseDTO>>> getAllBills(@PathVariable Long companyId) {
        List<Bill> bills = billService.getAllBills(companyId);
        List<BillResponseDTO> response = bills.stream().map(BillResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBill(@PathVariable Long id, @Valid @RequestBody DeleteBillRequest request) {
        String comments = request.getComments();
        billService.deleteBill(id, comments);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Bill deleted successfully"));
    }
}


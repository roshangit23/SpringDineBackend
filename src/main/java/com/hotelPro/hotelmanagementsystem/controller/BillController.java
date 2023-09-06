package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.BillResponseDTO;
import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.service.BillService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
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

    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResponse<BillResponseDTO>> createBill(@PathVariable Long orderId,
                                                                   @RequestParam(required = false) String discountCode) {
        Order order = orderService.findById(orderId);
        Bill bill = billService.saveBill(order, discountCode);
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

    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<BillResponseDTO>>> getAllBills(@PathVariable Long companyId) {
        List<Bill> bills = billService.getAllBills(companyId);
        List<BillResponseDTO> response = bills.stream().map(BillResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Bill deleted successfully"));
    }
}


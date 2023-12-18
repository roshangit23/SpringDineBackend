package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.Order;

import java.util.List;
import java.util.Map;

public interface BillService {
    Bill saveBill(Order order, Discount discount);
    Bill getBillById(Long id);
    List<Bill> getAllBills(Long companyId);
    void deleteBill(Long id,String comments);
    Bill settleBill(Long billId, Map<Bill.PaymentMode, Double> paymentModes);
    Bill findByBillNoAndCompanyId(Long billNo, Long companyId);
}

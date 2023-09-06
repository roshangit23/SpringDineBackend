package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Order;

import java.util.List;
import java.util.Map;

public interface BillService {
    Bill saveBill(Order order,String discountCode);
    Bill getBillById(Long id);
    //List<Bill> getAllBills();
    List<Bill> getAllBills(Long companyId);
    void deleteBill(Long id);
    Bill settleBill(Long billId, Map<Bill.PaymentMode, Double> paymentModes);
}

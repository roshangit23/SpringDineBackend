package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.repository.BillAuditRepository;
import com.hotelPro.hotelmanagementsystem.repository.BillRepository;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.OrderRepository;
import com.hotelPro.hotelmanagementsystem.service.BillService;
import com.hotelPro.hotelmanagementsystem.service.CustomerService;
import com.hotelPro.hotelmanagementsystem.service.DiscountService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import com.hotelPro.hotelmanagementsystem.util.GSTCalculator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BillAuditRepository billAuditRepository;
    @Override
    @Transactional
    public Bill saveBill(Order order,Discount discount) {

        if(order.getStatus()== Order.Status.IN_PROGRESS || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
            throw new CustomException("Bill cannot be created for orders which are in progress or removed", HttpStatus.BAD_REQUEST);
        }
        double amount = orderService.calculateTotal(order);
        // If a discount code is provided, look up the corresponding discount and apply it
        if (discount != null) {
            double discountAmount = orderService.calculateDiscount(order, discount);
            amount -= discountAmount; // Subtract the discount amount from the total
        }
        Bill bill = new Bill(order, Double.parseDouble(String.format("%.2f", amount)));
        if (discount != null) {
            bill.setDiscount(discount);
        }
        RestaurantSection.RestaurantType restaurantType = order.getRestaurantSection().getRestaurantType();
        if (restaurantType != RestaurantSection.RestaurantType.NO_GST) {  // Check if GST should be applied
            double gstAmount = 0.0;
            for (FoodItemOrder foodItemOrder : order.getFoodItemOrders()) {
                FoodItem.FoodType foodType = foodItemOrder.getFoodItem().getFoodType();
                double itemAmount = foodItemOrder.getFoodItem().getItemPrice() * foodItemOrder.getQuantity();
                gstAmount += GSTCalculator.calculateGST(itemAmount, restaurantType, foodType);  // Accumulate GST amount
            }
            double cgst = gstAmount / 2;
            double sgst = gstAmount / 2;

            bill.setCgst(Double.parseDouble(String.format("%.2f", cgst)));
            bill.setSgst(Double.parseDouble(String.format("%.2f", sgst)));
            bill.setTotalAmount(Double.parseDouble(String.format("%.2f", amount + gstAmount)));
            // Set total amount including GST
        } else {
            bill.setTotalAmount(Double.parseDouble(String.format("%.2f", amount)));  // Set total amount without GST as NO_GST is applied
            bill.setCgst(0.0);
            bill.setSgst(0.0);
        }

        bill.setCompany(order.getCompany());
        Long lastBillNo = billRepository.findMaxBillNoByCompany(order.getCompany().getId());
        bill.setBillNo(lastBillNo == null ? 1 : lastBillNo + 1);

        // If the order type is DELIVERY, then customer details are mandatory
        if (order.getType() == Order.OrderType.DELIVERY) {
            if (order.getCustomer() == null) {
                throw new CustomException("Customer details are mandatory for DELIVERY orders", HttpStatus.BAD_REQUEST);
            }
            bill.setCustomer(order.getCustomer());
        } else {
            // For other order types, customer details are optional, so we set them if they are available
            if (order.getCustomer() != null) {
                bill.setCustomer(order.getCustomer());
            }
        }
        if(order.getRestaurantTable()!=null){
            bill.setRestaurantTable(order.getRestaurantTable());
        }
        return billRepository.save(bill);
    }

    @Override
    @Transactional
    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));
    }

    @Override
    @Transactional
    public List<Bill> getAllBills(Long companyId) {
        return billRepository.findByCompanyId(companyId);
        //return billRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteBill(Long id, String comments) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));

        // Copy data from Bill to BillAudit
        BillAudit billAudit = new BillAudit();
        billAudit.setBillNo(bill.getBillNo());
        billAudit.setAmount(bill.getAmount());
        billAudit.setCgst(bill.getCgst());
        billAudit.setSgst(bill.getSgst());
        billAudit.setTotalAmount(bill.getTotalAmount());
        billAudit.setDueAmount(bill.getDueAmount());
        billAudit.setStatus(bill.getStatus());
        billAudit.setPaymentMode(bill.getPaymentMode());
        billAudit.setBillCreatedTime(bill.getBillCreatedTime());
        billAudit.setDeletedAt(LocalDateTime.now());
        billAudit.setComments(comments);
        billAuditRepository.save(billAudit);

        // If you have a bidirectional relationship, disassociate both sides
        Order order = bill.getOrder();
        if(order != null) {
            order.setBill(null); // disassociate Order from Bill
        }

        bill.setOrder(null); // disassociate Bill from Order

        billRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Bill settleBill(Long billId, Map<Bill.PaymentMode, Double> paymentModes) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", billId));

        double totalPaid = paymentModes.values().stream().mapToDouble(Double::doubleValue).sum();

        // Check if the payment mode contains DUE
        if (paymentModes.containsKey(Bill.PaymentMode.DUE)) {
            if (bill.getCustomer() == null) {
                throw new CustomException("Customer details are mandatory when payment mode is DUE.", HttpStatus.BAD_REQUEST);
            }
            if (totalPaid < bill.getTotalAmount()) {
                throw new CustomException("The total payment including DUE is less than the bill amount", HttpStatus.BAD_REQUEST);
            } else if (totalPaid > bill.getTotalAmount()) {
                throw new CustomException("The total payment including DUE is more than the bill amount", HttpStatus.BAD_REQUEST);
            }
            customerService.saveCustomerForBill(bill.getCustomer(), Bill.PaymentMode.DUE,bill.getCompany().getId());
            double dueAmount = paymentModes.get(Bill.PaymentMode.DUE);
            bill.setDueAmount(dueAmount);
            bill.setStatus(Bill.BillStatus.NOT_SETTLED);
        } else if (totalPaid < bill.getTotalAmount()) {
            throw new CustomException("The total payment is less than the bill amount", HttpStatus.BAD_REQUEST);
        } else if (totalPaid > bill.getTotalAmount()) {
            throw new CustomException("The total payment is more than the bill amount", HttpStatus.BAD_REQUEST);
        } else {
            bill.setDueAmount(null);
            bill.setStatus(Bill.BillStatus.SETTLED);
        }

        Set<Bill.PaymentMode> paymentModesSet = new HashSet<>(paymentModes.keySet());
        bill.setPaymentMode(paymentModesSet);
        return billRepository.save(bill);
    }

    @Transactional
    public Bill findByBillNoAndCompanyId(Long billNo, Long companyId) {
        return billRepository.findByBillNoAndCompanyId(billNo, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "Bill No", billNo));
    }

}

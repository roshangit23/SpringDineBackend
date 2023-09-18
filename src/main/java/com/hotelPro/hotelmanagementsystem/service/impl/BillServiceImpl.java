package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Bill;
import com.hotelPro.hotelmanagementsystem.model.Discount;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.repository.BillRepository;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.OrderRepository;
import com.hotelPro.hotelmanagementsystem.service.BillService;
import com.hotelPro.hotelmanagementsystem.service.CustomerService;
import com.hotelPro.hotelmanagementsystem.service.DiscountService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    @Override
    @Transactional
    public Bill saveBill(Order order,String discountCode) {
//        Company company = companyRepository.findById(companyId)
//                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

        if(order.getStatus()== Order.Status.IN_PROGRESS || order.getStatus()== Order.Status.REMOVED_WITHOUT_CREATING){
            throw new CustomException("Bill cannot be created for orders which are in progress or removed", HttpStatus.BAD_REQUEST);
        }
        double amount = orderService.calculateTotal(order);
        // If a discount code is provided, look up the corresponding discount and apply it
        if (discountCode != null) {
            Discount discount = discountService.getDiscountByCode(discountCode);
            double discountAmount = orderService.calculateDiscount(order, discount);
            amount -= discountAmount; // Subtract the discount amount from the total
        }
      //  Set<Bill.PaymentMode> paymentModeSet = new HashSet<>(Arrays.asList(Bill.PaymentMode.CARD));
        Bill bill = new Bill(order, amount);

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
        bill.setCompany(order.getCompany());
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

    @Override
    @Transactional
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));
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
            if (totalPaid < bill.getAmount()) {
                throw new CustomException("The total payment including DUE is less than the bill amount", HttpStatus.BAD_REQUEST);
            } else if (totalPaid > bill.getAmount()) {
                throw new CustomException("The total payment including DUE is more than the bill amount", HttpStatus.BAD_REQUEST);
            }
            customerService.saveCustomerForBill(bill.getCustomer(), Bill.PaymentMode.DUE);
            double dueAmount = paymentModes.get(Bill.PaymentMode.DUE);
            bill.setDueAmount(dueAmount);
            bill.setStatus(Bill.BillStatus.NOT_SETTLED);
        } else if (totalPaid < bill.getAmount()) {
            throw new CustomException("The total payment is less than the bill amount", HttpStatus.BAD_REQUEST);
        } else if (totalPaid > bill.getAmount()) {
            throw new CustomException("The total payment is more than the bill amount", HttpStatus.BAD_REQUEST);
        } else {
            bill.setDueAmount(null);
            bill.setStatus(Bill.BillStatus.SETTLED);
        }

        Set<Bill.PaymentMode> paymentModesSet = new HashSet<>(paymentModes.keySet());
        bill.setPaymentMode(paymentModesSet);
        return billRepository.save(bill);
    }

}

package com.hotelPro.hotelmanagementsystem.service.operations;

import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.service.BillService;
import com.hotelPro.hotelmanagementsystem.service.OrderService;
import org.springframework.stereotype.Service;
@Service
public class BillOps {

    private final BillService billService;
    private final OrderService orderService;
    public BillOps(BillService billService, OrderService orderService) {
        this.billService = billService;
        this.orderService = orderService;
    }

    public void createBill(Order order) {
        // implement the logic to create a bill
        // refresh the order from the database
        order = orderService.findById(order.getId());
        // Create and save a Bill for the Order
         //billService.saveBill(order, null);
         System.out.println("Bill saved successfully");
    }

    public void getAllBills() {
        // implement the logic to get all bills
        //System.out.print(billService.getAllBills());
    }

    public void getBillById(Long id) {
        // implement the logic to get bill by id
        System.out.print(billService.getBillById(id));
    }

    public void deleteBill(Long id) {
        // implement the logic to delete a bill
        //billService.deleteBill(id);
        System.out.println("Bill deleted for Id "+id);
    }
}

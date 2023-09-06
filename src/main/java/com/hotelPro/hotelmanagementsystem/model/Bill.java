package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bills")
public class Bill implements CompanyAssociatedEntity {

    public enum BillStatus {
        SETTLED, NOT_SETTLED
    }

    public enum PaymentMode {
        CASH, CARD, DUE, OTHER, PART
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(name = "amount")
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private Set<PaymentMode> paymentMode;
    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;
//    @Column(name = "people_count")
//    private int peopleCount;

    @CreationTimestamp
    @Column(name = "bill_created_time", updatable = false)
    private LocalDateTime billCreatedTime;
    public Bill() {}
    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = true)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public Bill(Order order, double amount) {
        this.order = order;
        this.amount = amount;
       // this.peopleCount = peopleCount;
    }
// Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public Set<PaymentMode> getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Set<PaymentMode> paymentMode) {
        this.paymentMode = paymentMode;
    }

//    public int getPeopleCount() {
//        return peopleCount;
//    }
//
//    public void setPeopleCount(int peopleCount) {
//        this.peopleCount = peopleCount;
//    }

    public void setBillCreatedTime(LocalDateTime billCreatedTime) {
        this.billCreatedTime = billCreatedTime;
    }

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Calculate the total amount for the bill
    public double calculateTotal() {
        double total = 0.0;
        Set<FoodItemOrder> foodItemOrders = order.getFoodItemOrders();
        for (FoodItemOrder itemOrder : foodItemOrders) {
            total += itemOrder.getQuantity() * itemOrder.getFoodItem().getItemPrice();
        }
        return total;
    }

    public LocalDateTime getBillCreatedTime() {
        return billCreatedTime;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", order=" + order +
                ", amount=" + amount +
                ", billCreatedTime=" + billCreatedTime +
                '}';
    }
}

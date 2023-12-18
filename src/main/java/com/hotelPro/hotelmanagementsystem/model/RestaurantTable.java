package com.hotelPro.hotelmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_table",
        uniqueConstraints = @UniqueConstraint(columnNames = {"table_number", "category","company_id"}))
public class RestaurantTable implements CompanyAssociatedEntity {

    public enum TableStatus {
        FREE, OCCUPIED, COMPLETED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TableStatus status;
    @NotBlank
    @Column(name = "category", nullable = false)
    private String category;
    @NotNull
    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private Order currentOrder;
    @JsonGetter("current_order_id")
    public Long getCurrentOrderId() {
        return (currentOrder != null) ? currentOrder.getId() : null;
    }
    @OneToMany(mappedBy = "restaurantTable", cascade = CascadeType.ALL)
    private List<Bill> bills;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // No-arg constructor for JPA
    public RestaurantTable() {
    }

    public RestaurantTable(TableStatus status, String category, Integer tableNumber, Order currentOrder) {
        this.status = status;
        this.category = category;
        this.tableNumber = tableNumber;
        this.currentOrder = currentOrder;
       // this.orders = new ArrayList<>();;
        this.bills = new ArrayList<>();
    }

// Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(List<Order> orders) {
//        this.orders = orders;
//    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

//    public void addOrder(Order order) {
//        this.orders.add(order);
//        order.setRestaurantTable(this);
//    }
//
//    public void removeOrder(Order order) {
//        this.orders.remove(order);
//        order.setRestaurantTable(null);
//    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void addBill(Bill bill) {
        this.bills.add(bill);
        bill.setRestaurantTable(this);
    }

    public void removeBill(Bill bill) {
        this.bills.remove(bill);
        bill.setRestaurantTable(null);
    }
}


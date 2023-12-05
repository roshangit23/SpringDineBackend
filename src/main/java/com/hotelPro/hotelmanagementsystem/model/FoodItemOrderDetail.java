package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "food_item_order_detail", uniqueConstraints = @UniqueConstraint(columnNames = {"kot_no", "company_id"}))
public class FoodItemOrderDetail implements CompanyAssociatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "kot_no")
    private Long kotNo;
    @ManyToOne
    @JoinColumn(name = "food_item_order_id")
    private FoodItemOrder foodItemOrder;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private FoodItemOrder.Status status;

    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private LocalDateTime place_time;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    public FoodItemOrderDetail() {
    }
    // getters, setters, and other methods...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodItemOrder getFoodItemOrder() {
        return foodItemOrder;
    }

    public void setFoodItemOrder(FoodItemOrder foodItemOrder) {
        this.foodItemOrder = foodItemOrder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public FoodItemOrder.Status getStatus() {
        return status;
    }

    public void setStatus(FoodItemOrder.Status status) {
        this.status = status;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public LocalDateTime getPlace_time() {
        return place_time;
    }

    public void setPlace_time(LocalDateTime place_time) {
        this.place_time = place_time;
    }

    public Long getKotNo() {
        return kotNo;
    }

    public void setKotNo(Long kotNo) {
        this.kotNo = kotNo;
    }

    @Override
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

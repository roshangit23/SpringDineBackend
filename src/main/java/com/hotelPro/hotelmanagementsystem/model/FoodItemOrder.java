package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "food_item_orders")
public class FoodItemOrder implements CompanyAssociatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    //@JsonBackReference
    private Order order;

    @Column(name = "quantity")
    private int quantity;
    @Column(name = "comments")
    private String comments;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private  LocalDateTime place_time;
    @OneToMany(mappedBy = "foodItemOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FoodItemOrderDetail> foodItemOrderDetails = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public enum Status {
      PLACED, IN_PROGRESS, COMPLETED, CANCELED
    }
    // constructors, getters, setters...

    public FoodItemOrder() {}

    public FoodItemOrder(FoodItem foodItem, Order order, int quantity) {
        this.foodItem = foodItem;
        this.order = order;
        this.quantity = quantity;
    }
    // getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    @Override
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<FoodItemOrderDetail> getFoodItemOrderDetails() {
        return foodItemOrderDetails;
    }

    public void setFoodItemOrderDetails(Set<FoodItemOrderDetail> foodItemOrderDetails) {
        this.foodItemOrderDetails = foodItemOrderDetails;
    }

    public void addFoodItemOrderDetail(FoodItemOrderDetail detail) {
        foodItemOrderDetails.add(detail);
        detail.setFoodItemOrder(this);
    }

    public void removeFoodItemOrderDetail(FoodItemOrderDetail detail) {
        foodItemOrderDetails.remove(detail);
        detail.setFoodItemOrder(null);
    }

    @Override
    public String toString() {
        return "FoodItemOrder{" +
                "id=" + id +
                ", foodItem=" + foodItem +
                ", order=" + order +
                ", quantity=" + quantity +
                '}';
    }
}
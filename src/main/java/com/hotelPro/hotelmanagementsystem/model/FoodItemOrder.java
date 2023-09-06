package com.hotelPro.hotelmanagementsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "food_item_orders")
public class FoodItemOrder {

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
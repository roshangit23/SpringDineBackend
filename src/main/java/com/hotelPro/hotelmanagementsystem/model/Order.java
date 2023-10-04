package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders", uniqueConstraints = @UniqueConstraint(columnNames = {"order_no", "company_id"}))
public class Order implements CompanyAssociatedEntity {
    public enum Status {
        IN_PROGRESS,
        MERGED, REMOVED_WITHOUT_CREATING, COMPLETED
    }
    public enum OrderType {
        TAKE_AWAY,
        DINE_IN,
        DELIVERY
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_no")
    private Long orderNo;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrderType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_section_id")
    private RestaurantSection restaurantSection;
    @Column(name = "comments")
    private String comments; // Additional comments about the order
    @Column
    private Integer customer_count;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    private Set<FoodItemOrder> foodItemOrders = new HashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = true)
    private Bill bill;

    @OneToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee assignedEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // constructors, getters, setters...
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Order() {}

    public Order(Status status, OrderType type) {
        //this.orderTime = orderTime;
        this.status = status;
        this.type = type;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Set<FoodItemOrder> getFoodItemOrders() {
        return foodItemOrders;
    }

    public void setFoodItemOrders(Set<FoodItemOrder> foodItemOrders) {
        this.foodItemOrders = foodItemOrders;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }
    public RestaurantSection getRestaurantSection() {
        return restaurantSection;
    }

    public void setRestaurantSection(RestaurantSection restaurantSection) {
        this.restaurantSection = restaurantSection;
    }
    public String getComments() {
        return comments;
    }

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void addFoodItemOrder(FoodItemOrder foodItemOrder) {
        foodItemOrders.add(foodItemOrder);
        foodItemOrder.setOrder(this);
    }

    public void removeFoodItemOrder(FoodItemOrder foodItemOrder) {
        foodItemOrders.remove(foodItemOrder);
        foodItemOrder.setOrder(null);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public Integer getCustomer_count() {
        return customer_count;
    }

    public void setCustomer_count(Integer customer_count) {
        this.customer_count = customer_count;
    }

    public Duration getOrderDuration() {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime);
        }
        return null;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                '}';
    }
}

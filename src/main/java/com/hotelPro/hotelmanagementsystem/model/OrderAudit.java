package com.hotelPro.hotelmanagementsystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_audit")
public class OrderAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no")
    private Long orderNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Order.Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Order.OrderType type;

    @Column(name = "comments")
    private String comments;

    @Column(name = "customer_count")
    private Integer customerCount;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "order_comments")
    private String orderComments;

    @Column(name = "restaurant_section_id")
    private Long restaurantSectionId;

    @Column(name = "restaurant_table_id")
    private Long restaurantTableId;

    @Lob
    @Column(name = "food_item_orders_summary")
    private String foodItemOrdersSummary;
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "employee_id")
    private Long employeeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    public OrderAudit() {
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

    public Order.Status getStatus() {
        return status;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }

    public Order.OrderType getType() {
        return type;
    }

    public void setType(Order.OrderType type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getOrderComments() {
        return orderComments;
    }

    public void setOrderComments(String orderComments) {
        this.orderComments = orderComments;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    public Long getRestaurantSectionId() {
        return restaurantSectionId;
    }

    public void setRestaurantSectionId(Long restaurantSectionId) {
        this.restaurantSectionId = restaurantSectionId;
    }

    public Long getRestaurantTableId() {
        return restaurantTableId;
    }

    public void setRestaurantTableId(Long restaurantTableId) {
        this.restaurantTableId = restaurantTableId;
    }

    public String getFoodItemOrdersSummary() {
        return foodItemOrdersSummary;
    }

    public void setFoodItemOrdersSummary(String foodItemOrdersSummary) {
        this.foodItemOrdersSummary = foodItemOrdersSummary;
    }
}


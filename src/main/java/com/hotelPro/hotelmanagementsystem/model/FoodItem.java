package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.*;

@Entity
@Table(name = "food_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"item_name", "company_id"}),
                @UniqueConstraint(columnNames = {"short_code1", "company_id"}),
                @UniqueConstraint(columnNames = {"short_code2", "company_id"})
        })
public class FoodItem implements CompanyAssociatedEntity {
    public enum FoodType {
        ALCOHOLIC, NON_ALCOHOLIC
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "item_name", nullable = false)
    private String itemName;
    @NotNull
    @Positive
    @Column(name = "item_price")
    private Double itemPrice;
    @NotBlank
    @Column(name = "category")
    private String category;
    @NotBlank
    @Column(name = "type")
    private String type;
    @Column(nullable = false, columnDefinition = "varchar(255) default 'NON_ALCOHOLIC'")
    @Enumerated(EnumType.STRING)
    private FoodType foodType = FoodType.NON_ALCOHOLIC;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "short_code1", nullable = true, unique = false)
    private String shortCode1;
    @Column(name = "short_code2", nullable = true, unique = false)
    private String shortCode2;
    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FoodItemOrder> foodItemOrders = new HashSet<>();

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodItemInventory> requiredInventoryItems = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public FoodItem() {}

    public FoodItem(String itemName, Double itemPrice, String category, String type, String description) {
        this(itemName, itemPrice, category, type, description, null, null);
    }

    public FoodItem(String itemName, Double itemPrice, String category, String type, String description, String shortCode1, String shortCode2) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.category = category;
        this.type = type;
        this.description = description;
        this.shortCode1 = shortCode1;
        this.shortCode2 = shortCode2;
    }

// getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortCode1() {
        return shortCode1;
    }

    public void setShortCode1(String shortCode1) {
        this.shortCode1 = shortCode1;
    }

    public String getShortCode2() {
        return shortCode2;
    }

    public void setShortCode2(String shortCode2) {
        this.shortCode2 = shortCode2;
    }

    public Set<FoodItemOrder> getFoodItemOrders() {
        return foodItemOrders;
    }

    public void setFoodItemOrders(Set<FoodItemOrder> foodItemOrders) {
        this.foodItemOrders = foodItemOrders;
    }

    public List<FoodItemInventory> getRequiredInventoryItems() {
        return requiredInventoryItems;
    }

    public void setRequiredInventoryItems(List<FoodItemInventory> requiredInventoryItems) {
        this.requiredInventoryItems = requiredInventoryItems;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}

package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "food_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"item_name", "company_id"}))
public class FoodItem implements CompanyAssociatedEntity {

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
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FoodItemOrder> foodItemOrders = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public FoodItem() {}

    public FoodItem(String itemName, Double itemPrice, String category, String type, String description) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.category = category;
        this.type = type;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<FoodItemOrder> getFoodItemOrders() {
        return foodItemOrders;
    }

    public void setFoodItemOrders(Set<FoodItemOrder> foodItemOrders) {
        this.foodItemOrders = foodItemOrders;
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

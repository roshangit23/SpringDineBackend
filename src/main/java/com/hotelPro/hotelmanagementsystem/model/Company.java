package com.hotelPro.hotelmanagementsystem.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String postalCode;
    @Column
    private String phoneNumber;
    @Column
    private String email;
    @Column
    private String logoUrl;
    @Column
    private String taxIdentificationNumber;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "company_restaurant_section",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_section_id")
    )
    private Set<RestaurantSection> restaurantSections = new HashSet<>();

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL)
    private Subscription subscription;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users;

    // New many-to-many relationship
    @ManyToMany(mappedBy = "companies")
    private Set<User> dashboardUsers = new HashSet<>();

    // Constructors, Getters, Setters, etc.
    public Company(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public Set<RestaurantSection> getRestaurantSections() {
        return restaurantSections;
    }

    public void setRestaurantSections(Set<RestaurantSection> restaurantSections) {
        this.restaurantSections = restaurantSections;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Set<User> getDashboardUsers() {
        return dashboardUsers;
    }

    public void setDashboardUsers(Set<User> dashboardUsers) {
        this.dashboardUsers = dashboardUsers;
    }
    public void addDashboardUser(User user) {
        this.dashboardUsers.add(user);
        user.getCompanies().add(this);
    }

    public void removeDashboardUser(User user) {
        this.dashboardUsers.remove(user);
        user.getCompanies().remove(this);
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

}


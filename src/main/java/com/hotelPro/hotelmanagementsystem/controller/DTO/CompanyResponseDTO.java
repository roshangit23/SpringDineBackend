package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.RestaurantSection;

import java.util.Set;
import java.util.stream.Collectors;

public class CompanyResponseDTO {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String logoUrl;
    private String taxIdentificationNumber;
    private Set<RestaurantSectionDTO> restaurantSections;

    // Constructors
    public CompanyResponseDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.address = company.getAddress();
        this.city = company.getCity();
        this.state = company.getState();
        this.country = company.getCountry();
        this.postalCode = company.getPostalCode();
        this.phoneNumber = company.getPhoneNumber();
        this.email = company.getEmail();
        this.logoUrl = company.getLogoUrl();
        this.taxIdentificationNumber = company.getTaxIdentificationNumber();
        this.restaurantSections = company.getRestaurantSections()
                .stream()
                .map(section -> new RestaurantSectionDTO(section))
                .collect(Collectors.toSet());
    }

    // Getters and setters for each field

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

    public Set<RestaurantSectionDTO> getRestaurantSections() {
        return restaurantSections;
    }

    public void setRestaurantSections(Set<RestaurantSectionDTO> restaurantSections) {
        this.restaurantSections = restaurantSections;
    }

    // Inner class for RestaurantSectionDTO
    public static class RestaurantSectionDTO {
        private Long id;
        private String restaurantType;

        public RestaurantSectionDTO(RestaurantSection section) {
            this.id = section.getId();
            this.restaurantType = section.getRestaurantType().toString();
        }

        // Getters and setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getRestaurantType() {
            return restaurantType;
        }

        public void setRestaurantType(String restaurantType) {
            this.restaurantType = restaurantType;
        }
    }

    // Getters and Setters for all fields
}

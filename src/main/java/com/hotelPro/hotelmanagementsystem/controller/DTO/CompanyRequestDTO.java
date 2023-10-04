package com.hotelPro.hotelmanagementsystem.controller.DTO;

import com.hotelPro.hotelmanagementsystem.model.RestaurantSection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

public class CompanyRequestDTO {
   @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String country;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String email;
    private String logoUrl;
    private String taxIdentificationNumber;
    @NotEmpty
    private Set<RestaurantSectionDTO> restaurantSections = new HashSet<>();

    // Default constructor
    public CompanyRequestDTO() {}

    // Getters and setters

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

    public void setRestaurantSections(Set<RestaurantSectionDTO> restaurantSections) {
        this.restaurantSections = restaurantSections;
    }

    public Set<RestaurantSectionDTO> getRestaurantSections() {
        return restaurantSections;
    }

    public static class RestaurantSectionDTO {
        private RestaurantSection.RestaurantType restaurantType;

        public RestaurantSectionDTO() {
        }

        // Getters and setters
        public RestaurantSection.RestaurantType getRestaurantType() {
            return restaurantType;
        }

        public void setRestaurantType(RestaurantSection.RestaurantType restaurantType) {
            this.restaurantType = restaurantType;
        }
    }
}

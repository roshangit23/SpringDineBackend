package com.hotelPro.hotelmanagementsystem.controller.DTO;

import jakarta.validation.constraints.*;

import java.util.Set;

public class UserRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, message = "Password should be at least 8 characters.")
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number format.")
    private String mobileNumber;
    @NotEmpty
    private Set<String> roles;
    @NotNull
    private Long companyId;

    // Constructors
    public UserRequestDTO() {}

    public UserRequestDTO(String username, String password,String firstName, String lastName, String email, String mobileNumber, Set<String> roles, Long companyId) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.roles = roles;
        this.companyId = companyId;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}

package com.hotelPro.hotelmanagementsystem.model;

import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "employee",
     uniqueConstraints = @UniqueConstraint(columnNames = {"phone_number", "company_id"}))
public class Employee implements CompanyAssociatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String position;
    private double salary;
    @Column(name = "phone_number")
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    // Constructors
    public Employee() {
        // default constructor
    }

    public Employee(String firstName, String lastName, String position, double salary, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.salary = salary;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

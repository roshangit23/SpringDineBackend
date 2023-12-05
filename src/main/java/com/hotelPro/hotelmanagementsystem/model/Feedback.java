package com.hotelPro.hotelmanagementsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // No-arg constructor
    public Feedback() {
    }

    // Constructor with all fields
    public Feedback(String content, Company company) {
        this.content = content;
        this.company = company;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Company getCompany() {
        return company;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}


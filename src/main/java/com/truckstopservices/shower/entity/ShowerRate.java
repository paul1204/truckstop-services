package com.truckstopservices.shower.entity;

import jakarta.persistence.*;

@Entity
public class ShowerRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double rate;
    private String description;
    private int maxHours;
    
    // Default constructor
    public ShowerRate() {}
    
    // Parameterized constructor
    public ShowerRate(double rate, String description, int maxHours) {
        this.rate = rate;
        this.description = description;
        this.maxHours = maxHours;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public double getRate() {
        return rate;
    }
    
    public void setRate(double rate) {
        this.rate = rate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getMaxHours() {
        return maxHours;
    }
    
    public void setMaxHours(int maxHours) {
        this.maxHours = maxHours;
    }
}
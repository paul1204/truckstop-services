package com.truckstopservices.parking.entity;

import jakarta.persistence.*;

@Entity
public class ParkingRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private RateType rateType;
    
    private double rate;
    private String description;
    
    // Default constructor
    public ParkingRate() {}
    
    // Parameterized constructor
    public ParkingRate(RateType rateType, double rate, String description) {
        this.rateType = rateType;
        this.rate = rate;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public RateType getRateType() {
        return rateType;
    }
    
    public void setRateType(RateType rateType) {
        this.rateType = rateType;
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
    
    // Enum for rate types
    public enum RateType {
        HOURLY,
        NIGHTLY,
        WEEKLY
    }
}
package com.truckstopservices.shower.entity;

import jakarta.persistence.*;

@Entity
public class Shower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String showerNumber;
    private boolean occupied;
    private boolean cleaning;
    private String occupiedSince;
    private String reservedUntil;
    private String cleaningUntil;
    private String customerName;
    
    // Default constructor
    public Shower() {}
    
    // Parameterized constructor
    public Shower(String showerNumber, boolean occupied, boolean cleaning, 
                 String occupiedSince, String reservedUntil, 
                 String cleaningUntil, String customerName) {
        this.showerNumber = showerNumber;
        this.occupied = occupied;
        this.cleaning = cleaning;
        this.occupiedSince = occupiedSince;
        this.reservedUntil = reservedUntil;
        this.cleaningUntil = cleaningUntil;
        this.customerName = customerName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public String getShowerNumber() {
        return showerNumber;
    }
    
    public void setShowerNumber(String showerNumber) {
        this.showerNumber = showerNumber;
    }
    
    public boolean isOccupied() {
        return occupied;
    }
    
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    
    public boolean isCleaning() {
        return cleaning;
    }
    
    public void setCleaning(boolean cleaning) {
        this.cleaning = cleaning;
    }
    
    public String getOccupiedSince() {
        return occupiedSince;
    }
    
    public void setOccupiedSince(String occupiedSince) {
        this.occupiedSince = occupiedSince;
    }
    
    public String getReservedUntil() {
        return reservedUntil;
    }
    
    public void setReservedUntil(String reservedUntil) {
        this.reservedUntil = reservedUntil;
    }
    
    public String getCleaningUntil() {
        return cleaningUntil;
    }
    
    public void setCleaningUntil(String cleaningUntil) {
        this.cleaningUntil = cleaningUntil;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
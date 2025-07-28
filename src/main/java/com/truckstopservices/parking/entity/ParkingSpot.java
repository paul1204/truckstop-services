package com.truckstopservices.parking.entity;

import jakarta.persistence.*;

@Entity
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String spotNumber;
    private boolean occupied;
    private String occupiedSince;
    private String reservedUntil;
    private String vehicleRegistration;
    
    // Default constructor
    public ParkingSpot() {}
    
    // Parameterized constructor
    public ParkingSpot(String spotNumber, boolean occupied, String occupiedSince, 
                       String reservedUntil, String vehicleRegistration) {
        this.spotNumber = spotNumber;
        this.occupied = occupied;
        this.occupiedSince = occupiedSince;
        this.reservedUntil = reservedUntil;
        this.vehicleRegistration = vehicleRegistration;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public String getSpotNumber() {
        return spotNumber;
    }
    
    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }
    
    public boolean isOccupied() {
        return occupied;
    }
    
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
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
    
    public String getVehicleRegistration() {
        return vehicleRegistration;
    }
    
    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }
}
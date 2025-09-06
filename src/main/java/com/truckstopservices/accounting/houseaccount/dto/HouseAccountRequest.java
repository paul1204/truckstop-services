package com.truckstopservices.accounting.houseaccount.dto;

/**
 * DTO for creating or updating a house account.
 */
public class HouseAccountRequest {
    private String companyName; // Optional business identifier, not used as PK anymore
    private String phoneNumber;
    private String address;
    private Double creditLimit;
    
    // Default constructor
    public HouseAccountRequest() {
    }
    
//    public HouseAccountRequest(String phoneNumber, String address, Double creditLimit) {
//        this.phoneNumber = phoneNumber;
//        this.address = address;
//        this.creditLimit = creditLimit;
//    }
    
    public HouseAccountRequest(String companyName, String phoneNumber, String address, Double creditLimit) {
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.creditLimit = creditLimit;
    }
    
    // Getters and setters
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Double getCreditLimit() {
        return creditLimit;
    }
    
    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }
}
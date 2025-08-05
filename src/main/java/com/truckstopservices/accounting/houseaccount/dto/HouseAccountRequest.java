package com.truckstopservices.accounting.houseaccount.dto;

/**
 * DTO for creating or updating a house account.
 */
public class HouseAccountRequest {
    private String customerNumber; // UID
    private String name;
    private String phoneNumber;
    private String address;
    private Double creditLimit;
    
    // Default constructor
    public HouseAccountRequest() {
    }
    
    public HouseAccountRequest(String customerNumber, String name, String phoneNumber, String address, Double creditLimit) {
        this.customerNumber = customerNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.creditLimit = creditLimit;
    }
    
    // Getters and setters
    public String getCustomerNumber() {
        return customerNumber;
    }
    
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
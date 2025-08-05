package com.truckstopservices.accounting.houseaccount.dto;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;

import java.time.LocalDateTime;

/**
 * DTO for house account responses.
 */
public class HouseAccountResponse {
    private String customerNumber;
    private String name;
    private String phoneNumber;
    private String address;
    private Double creditLimit;
    private AccountStanding accountStanding;
    private Integer goodStandingDuration;
    private Integer accountAge;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Default constructor
    public HouseAccountResponse() {
    }
    
    /**
     * Constructor to create a response from an entity.
     * 
     * @param houseAccount The house account entity
     */
    public HouseAccountResponse(HouseAccount houseAccount) {
        this.customerNumber = houseAccount.getCustomerNumber();
        this.name = houseAccount.getName();
        this.phoneNumber = houseAccount.getPhoneNumber();
        this.address = houseAccount.getAddress();
        this.creditLimit = houseAccount.getCreditLimit();
        this.accountStanding = houseAccount.getAccountStanding();
        this.goodStandingDuration = houseAccount.getGoodStandingDuration();
        this.accountAge = houseAccount.getAccountAge();
        this.createdAt = houseAccount.getCreatedAt();
        this.updatedAt = houseAccount.getUpdatedAt();
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
    
    public AccountStanding getAccountStanding() {
        return accountStanding;
    }
    
    public void setAccountStanding(AccountStanding accountStanding) {
        this.accountStanding = accountStanding;
    }
    
    public Integer getGoodStandingDuration() {
        return goodStandingDuration;
    }
    
    public void setGoodStandingDuration(Integer goodStandingDuration) {
        this.goodStandingDuration = goodStandingDuration;
    }
    
    public Integer getAccountAge() {
        return accountAge;
    }
    
    public void setAccountAge(Integer accountAge) {
        this.accountAge = accountAge;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Static factory method to create a response from an entity.
     * 
     * @param houseAccount The house account entity
     * @return A new HouseAccountResponse
     */
    public static HouseAccountResponse fromEntity(HouseAccount houseAccount) {
        return new HouseAccountResponse(houseAccount);
    }
}
package com.truckstopservices.accounting.houseaccount.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a house account for local companies to pay later.
 * Each account has a credit limit of $1000 per week.
 */
@Entity
@Table(name = "house_accounts")
public class HouseAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String houseAccountId; // Auto-generated UID
    
    private String companyName;
    private String phoneNumber;
    private String address;
    
    @Column(name = "credit_limit")
    private Double creditLimit = 1000.00; // Default credit limit of $1000 per week

    @Column(name = "amount_due")
    private Double amountDue = 0.00;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_standing")
    private AccountStanding accountStanding = AccountStanding.GOOD; // Default to good standing
    
    @Column(name = "good_standing_duration")
    private Integer goodStandingDuration = 0; // Duration in days of being in good standing
    
    @Column(name = "account_age")
    private Integer accountAge = 0; // Duration in days of account being open
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Enum representing the standing status of a house account.
     */
    public enum AccountStanding {
        GOOD,
        PAST_DUE,
        OVER_DUE
    }
    
    // Default constructor required by JPA
    public HouseAccount() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    public HouseAccount(String companyName, String phoneNumber, String address) {
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getHouseAccountId() {
        return houseAccountId;
    }
    
    public void setHouseAccountId(String houseAccountId) {
        this.houseAccountId = houseAccountId;
    }
    
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

    public Double getAmountDue() { return amountDue;}

    public void setAmountDue(Double amountDue) {this.amountDue = amountDue;}

    public AccountStanding getAccountStanding() {
        return accountStanding;
    }
    
    public void setAccountStanding(AccountStanding accountStanding) {
        // Reset good standing duration if account becomes past due
        if (this.accountStanding == AccountStanding.GOOD && 
            (accountStanding == AccountStanding.PAST_DUE || accountStanding == AccountStanding.OVER_DUE)) {
            this.goodStandingDuration = 0;
        }
        this.accountStanding = accountStanding;
        this.updatedAt = LocalDateTime.now();
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
     * Increment the account age by one day.
     */
    public void incrementAccountAge() {
        this.accountAge++;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Increment the good standing duration by one day if the account is in good standing.
     */
    public void incrementGoodStandingDuration() {
        if (this.accountStanding == AccountStanding.GOOD) {
            this.goodStandingDuration++;
        }
        this.updatedAt = LocalDateTime.now();
    }
}
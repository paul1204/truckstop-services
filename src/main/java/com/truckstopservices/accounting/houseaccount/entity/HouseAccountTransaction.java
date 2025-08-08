package com.truckstopservices.accounting.houseaccount.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "house_account_transactions")
public class HouseAccountTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;
    
    @Column(name = "house_account_number", nullable = false)
    private String houseAccountNumber;
    
    @ManyToOne
    @JoinColumn(name = "house_account_number", referencedColumnName = "customerNumber", insertable = false, updatable = false)
    private HouseAccount houseAccount;
    
    @Column(name = "date_of_purchase", nullable = false)
    private LocalDate dateOfPurchase;
    
    @Column(name = "date_due", nullable = false)
    private LocalDate dateDue;
    
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Column(name = "gallons", nullable = false)
    private Double gallons;

    public HouseAccountTransaction() {
//        this.dateOfPurchase = LocalDate.now();
//        this.dateDue = dateOfPurchase.plusDays(7);
    }
    
    public HouseAccountTransaction(String invoiceNumber, String houseAccountNumber, Double amount, Double gallons) {
        this.invoiceNumber = invoiceNumber;
        this.houseAccountNumber = houseAccountNumber;
        this.amount = amount;
        this.gallons = gallons;
        this.dateOfPurchase = LocalDate.now();
        this.dateDue = dateOfPurchase.plusDays(7);
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    public String getHouseAccountNumber() {
        return houseAccountNumber;
    }
    
    public void setHouseAccountNumber(String houseAccountNumber) {
        this.houseAccountNumber = houseAccountNumber;
    }
    
    public HouseAccount getHouseAccount() {
        return houseAccount;
    }
    
    public void setHouseAccount(HouseAccount houseAccount) {
        this.houseAccount = houseAccount;
        if (houseAccount != null) {
            this.houseAccountNumber = houseAccount.getCustomerNumber();
        }
    }
    
    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }
    
    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
        this.dateDue = dateOfPurchase.plusDays(7);
    }
    
    public LocalDate getDateDue() {
        return dateDue;
    }
    
    public void setDateDue(LocalDate dateDue) {
        this.dateDue = dateDue;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public Double getGallons() {
        return gallons;
    }
    
    public void setGallons(Double gallons) {
        this.gallons = gallons;
    }
}
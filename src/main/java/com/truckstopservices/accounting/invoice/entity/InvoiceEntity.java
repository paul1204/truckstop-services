package com.truckstopservices.accounting.invoice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {
    
    @Id
    private String invoiceNumber;
    
    private String companyName;
    private String date;
    private Double amount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor required by JPA
    public InvoiceEntity() {
    }
    
    public InvoiceEntity(String invoiceNumber, String companyName, String date, Double amount) {
        this.invoiceNumber = invoiceNumber;
        this.companyName = companyName;
        this.date = date;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
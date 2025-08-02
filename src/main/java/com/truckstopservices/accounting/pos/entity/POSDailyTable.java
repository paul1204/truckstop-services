package com.truckstopservices.accounting.pos.entity;

import com.truckstopservices.accounting.pos.enums.SalesType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pos_daily_table")
public class POSDailyTable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String receiptId;
    
    @Column(nullable = false)
    private LocalDateTime salesDate;
    
    @Column(nullable = false)
    private double salesAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalesType salesType;
    
    // Default constructor
    public POSDailyTable() {
    }
    
    // Constructor with parameters
    public POSDailyTable(LocalDateTime salesDate, double salesAmount, SalesType salesType) {
        this.salesDate = salesDate;
        this.salesAmount = salesAmount;
        this.salesType = salesType;
    }
    
    // Getters and setters
    public String getReceiptId() {
        return receiptId;
    }
    
    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
    
    public LocalDateTime getSalesDate() {
        return salesDate;
    }
    
    public void setSalesDate(LocalDateTime salesDate) {
        this.salesDate = salesDate;
    }
    
    public double getSalesAmount() {
        return salesAmount;
    }
    
    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }
    
    public SalesType getSalesType() {
        return salesType;
    }
    
    public void setSalesType(SalesType salesType) {
        this.salesType = salesType;
    }
}
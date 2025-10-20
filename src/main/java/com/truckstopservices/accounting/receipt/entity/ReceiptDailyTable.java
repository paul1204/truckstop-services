package com.truckstopservices.accounting.receipt.entity;

import com.truckstopservices.accounting.receipt.enums.SalesType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipt_daily_table")
public class ReceiptDailyTable {
    
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
    public ReceiptDailyTable() {
    }
    
    // Constructor with parameters
    public ReceiptDailyTable(LocalDateTime salesDate, double salesAmount, SalesType salesType) {
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

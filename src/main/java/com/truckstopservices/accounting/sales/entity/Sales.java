package com.truckstopservices.accounting.sales.entity;

import com.truckstopservices.common.types.SalesType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sales_id", updatable = false, nullable = false)
    private String salesId;

    @Column(name = "sales_date", nullable = false)
    private LocalDate salesDate;

    @Column(name = "sales_time", nullable = false)
    private LocalTime salesTime;

    @Column(name = "sales_amount", nullable = false)
    private Double salesAmount;

 //   private SalesType salesType;
    @Enumerated(EnumType.STRING)
    @Column(name = "sales_type", nullable = false)
    private SalesType salesType;

    @Column(name = "shift_number")
    private Integer shiftNumber;

    public Sales() {
    }

    public Sales(LocalDate salesDate, LocalTime salesTime, Double salesAmount, SalesType salesType) {
        this.salesDate = salesDate;
        this.salesTime = salesTime;
        this.salesAmount = salesAmount;
        this.salesType = salesType;
    }

    public Sales(LocalDate salesDate, LocalTime salesTime, Double salesAmount, SalesType salesType, Integer shiftNumber) {
        this.salesDate = salesDate;
        this.salesTime = salesTime;
        this.salesAmount = salesAmount;
        this.salesType = salesType;
        this.shiftNumber = shiftNumber;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    public LocalTime getSalesTime() {
        return salesTime;
    }

    public void setSalesTime(LocalTime salesTime) {
        this.salesTime = salesTime;
    }

    public Double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public SalesType getSalesType() {
        return salesType;
    }

    public void setSalesType(SalesType salesType) {
        this.salesType = salesType;
    }

    public Integer getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(Integer shiftNumber) {
        this.shiftNumber = shiftNumber;
    }
}

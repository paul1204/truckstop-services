package com.truckstopservices.accounting.sales.entity;

import com.truckstopservices.common.types.SalesType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "shift_number")
    private Integer shiftNumber;

    @Column(name = "terminal")
    private String terminal;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesItem> salesItems;

    public Sales() {
    }

    public Sales(LocalDate salesDate, LocalTime salesTime, Double salesAmount,
                 Integer shiftNumber, ArrayList<SalesItem> salesItems) {
        this.salesDate = salesDate;
        this.salesTime = salesTime;
        this.salesAmount = salesAmount;
        this.shiftNumber = shiftNumber;
        this.salesItems = salesItems;
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

    public Integer getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(Integer shiftNumber) {
        this.shiftNumber = shiftNumber;
    }
    
    public String getTerminal() {
        return terminal;
    }
    
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
    
    public List<SalesItem> getSalesItems() {return salesItems;}

    public void setSalesItems(List<SalesItem> salesItems) {this.salesItems = salesItems;}

    public void addSalesItem(SalesItem item) {
        if (salesItems == null) {
            salesItems = new ArrayList<>();
        }
        salesItems.add(item);
        item.setSale(this); // This is the magic line that fixes the NULL in DB
    }
}

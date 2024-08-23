package com.truckstopservices.processing.entity;


import jakarta.persistence.*;

@Entity
public class MerchandiseSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long merchandiseSalesId;

    private double merchandiseSales;
    private double nonRestaurantSales;
    private double bottledBeverageSales;

    @OneToOne
    @JoinColumn(name = "shift_number", referencedColumnName = "shiftNumber")
    private ShiftReport shiftReport;

    public double getMerchandiseSales() {
        return merchandiseSales;
    }

    public void setMerchandiseSales(double merchandiseSales) {
        this.merchandiseSales = merchandiseSales;
    }

    public MerchandiseSales(){
    }
    public MerchandiseSales(double merchandiseSales) {
        this.merchandiseSales = merchandiseSales;
    }

    public void setShiftReport(ShiftReport shiftReport) {
        this.shiftReport = shiftReport;
    }
    //Break Down Convenience Store Items later...

    public double getNonRestaurantSales() {
        return nonRestaurantSales;
    }

    public void setNonRestaurantSales(double nonRestaurantSales) {
        this.nonRestaurantSales = nonRestaurantSales;
    }

    public double getBottledBeverageSales() {
        return bottledBeverageSales;
    }

    public void setBottledBeverageSales(double bottledBeverageSales) {
        this.bottledBeverageSales = bottledBeverageSales;
    }
}

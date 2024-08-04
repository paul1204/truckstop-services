package com.truckstopservices.processing.entity;


import jakarta.persistence.*;


@Entity
public class FuelSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fuelSalesId;
    private double dieselTransactions;
    private double regularGasolineTransactions;
    private double midGradeGasolineTransactions;
    private double premiumGasolineTransactions;
    private double totalGasolineSales;


    @OneToOne
    //@JoinColumn(name = "shift_id")
    @JoinColumn(name = "shift_number", referencedColumnName = "shiftNumber")
    private ShiftReport shiftReport;

    public FuelSales(){

    }
    public FuelSales(double totalFuelSales){
        this.totalGasolineSales = totalFuelSales;

    }

    public double getDieselTransactions() {
        return dieselTransactions;
    }

    public void setDieselTransactions(double dieselTransactions) {
        this.dieselTransactions = dieselTransactions;
    }

    public double getRegularGasolineTransactions() {
        return regularGasolineTransactions;
    }

    public void setRegularGasolineTransactions(double regularGasolineTransactions) {
        this.regularGasolineTransactions = regularGasolineTransactions;
    }

    public double getMidGradeGasolineTransactions() {
        return midGradeGasolineTransactions;
    }

    public void setMidGradeGasolineTransactions(double midGradeGasolineTransactions) {
        this.midGradeGasolineTransactions = midGradeGasolineTransactions;
    }

    public double getPremiumGasolineTransactions() {
        return premiumGasolineTransactions;
    }

    public void setPremiumGasolineTransactions(double premiumGasolineTransactions) {
        this.premiumGasolineTransactions = premiumGasolineTransactions;
    }

    public double getTotalGasolineSales() {
        return totalGasolineSales;
    }

    public void setTotalGasolineSales(double totalGasolineSales) {
        this.totalGasolineSales = totalGasolineSales;
    }

    public ShiftReport getShiftReport() {
        return shiftReport;
    }

    public void setShiftReport(ShiftReport shiftReport) {
        this.shiftReport = shiftReport;
    }
}

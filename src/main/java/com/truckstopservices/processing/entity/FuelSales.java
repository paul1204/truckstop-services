package com.truckstopservices.processing.entity;


//import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
@Entity
public class FuelSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fuelId;
    private int dieselTransactions;
    private int regularGasolineTransactions;
    private int midGradeGasolineTransactions;
    private int premiumGasolineTransactions;
    private double totalGasolineSales;

    @ManyToOne
    private ShiftReport shiftReport;


    public int getDieselTransactions() {
        return dieselTransactions;
    }

    public void setDieselTransactions(int dieselTransactions) {
        this.dieselTransactions = dieselTransactions;
    }

    public int getRegularGasolineTransactions() {
        return regularGasolineTransactions;
    }

    public void setRegularGasolineTransactions(int regularGasolineTransactions) {
        this.regularGasolineTransactions = regularGasolineTransactions;
    }

    public int getMidGradeGasolineTransactions() {
        return midGradeGasolineTransactions;
    }

    public void setMidGradeGasolineTransactions(int midGradeGasolineTransactions) {
        this.midGradeGasolineTransactions = midGradeGasolineTransactions;
    }

    public int getPremiumGasolineTransactions() {
        return premiumGasolineTransactions;
    }

    public void setPremiumGasolineTransactions(int premiumGasolineTransactions) {
        this.premiumGasolineTransactions = premiumGasolineTransactions;
    }

    public double getTotalGasolineSales() {
        return totalGasolineSales;
    }

    public void setTotalGasolineSales(double totalGasolineSales) {
        this.totalGasolineSales = totalGasolineSales;
    }
}

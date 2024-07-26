package com.truckstopservices.processing.entity;

//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.OneToMany;
//import javax.persistence.CascadeType;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ShiftReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shiftId;
    private String date;
    private String shiftNumber;
    private String employeeID;
    private String managerID;
    private double posCashTil1;
    private double posCashTil2;
    public ShiftReport(){

    }
    @OneToMany(mappedBy = "shiftReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FuelSales> fuelSales;

    @OneToMany(mappedBy = "shiftReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MerchandiseSales> merchandiseSales;

    @OneToMany(mappedBy = "shiftReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantSales> restaurantSales;

    @OneToMany(mappedBy = "shiftReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TobaccoSales> tobacco;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(String shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    public double getPosCashTil1() {
        return posCashTil1;
    }

    public void setPosCashTil1(double posCashTil1) {
        this.posCashTil1 = posCashTil1;
    }

    public double getPosCashTil2() {
        return posCashTil2;
    }

    public void setPosCashTil2(double posCashTil2) {
        this.posCashTil2 = posCashTil2;
    }
}

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

    @Column(unique = true, nullable = false)
    private String shiftNumber; // Business identifier
    private String date;

    private String employeeID;
    private String managerID;
    private double posCashTil1;
    private double posCashTil2;
    public ShiftReport(){

    }

    @OneToOne(mappedBy = "shiftReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private FuelSales fuelSales;

    @OneToOne(mappedBy = "shiftReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private MerchandiseSales merchandiseSales;

    @OneToOne(mappedBy = "shiftReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private RestaurantSales restaurantSales;

    @OneToOne(mappedBy = "shiftReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private TobaccoSales tobaccoSales;

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

    public FuelSales getFuelSales() {
        return fuelSales;
    }

    public void setFuelSales(FuelSales fuelSales) {
        this.fuelSales = fuelSales;
    }

    public MerchandiseSales getMerchandiseSales() {
        return merchandiseSales;
    }

    public void setMerchandiseSales(MerchandiseSales merchandiseSales) {
        this.merchandiseSales = merchandiseSales;
    }

    public RestaurantSales getRestaurantSales() {
        return restaurantSales;
    }

    public void setRestaurantSales(RestaurantSales restaurantSales) {
        this.restaurantSales = restaurantSales;
    }

    public TobaccoSales getTobaccoSales() {
        return tobaccoSales;
    }

    public void setTobaccoSales(TobaccoSales tobacco) {
        this.tobaccoSales = tobacco;
    }

    public ShiftReport(String date, String shiftNumber, String employeeID, String managerID, double posCashTil1, double posCashTil2,
                            FuelSales fuelSales, MerchandiseSales merchandiseSales, RestaurantSales restaurantSales, TobaccoSales tobacco) {
        this.date = date;
        this.shiftNumber = shiftNumber;
        this.employeeID = employeeID;
        this.managerID = managerID;
        this.posCashTil1 = posCashTil1;
        this.posCashTil2 = posCashTil2;
        this.fuelSales = fuelSales;
        this.merchandiseSales = merchandiseSales;
        this.restaurantSales = restaurantSales;
        this.tobaccoSales = tobacco;
    }
}

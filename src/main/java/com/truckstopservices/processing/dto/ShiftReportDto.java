package com.truckstopservices.processing.dto;

public class ShiftReportDto {
    private String date;
    private String shiftNumber;
    private String employeeID;
    private String managerID;
    private double posCashTil1;
    private double posCashTil2;
    private double fuelSaleRegular;
    private double fuelSalesMidGrade;
    private double fuelSalesPremium;
    private double fuelSalesDiesel;
    private double convenienceStoreSales;
    private double restaurantSales;
    private double tobaccoSales;

    public ShiftReportDto(){

    }
    public ShiftReportDto(String date, String shiftNumber, String employeeID, String managerID, double posCashTil1, double posCashTil2, double fuelSaleRegular, double fuelSalesMidGrade, double fuelSalesPremium, double fuelSalesDiesel, double convenienceStoreSales, double restaurantSales, double tobaccoSales) {
        this.date = date;
        this.shiftNumber = shiftNumber;
        this.employeeID = employeeID;
        this.managerID = managerID;
        this.posCashTil1 = posCashTil1;
        this.posCashTil2 = posCashTil2;
        this.fuelSaleRegular = fuelSaleRegular;
        this.fuelSalesMidGrade = fuelSalesMidGrade;
        this.fuelSalesPremium = fuelSalesPremium;
        this.fuelSalesDiesel = fuelSalesDiesel;
        this.convenienceStoreSales = convenienceStoreSales;
        this.restaurantSales = restaurantSales;
        this.tobaccoSales = tobaccoSales;
    }

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

    public double getFuelSaleRegular() {
        return fuelSaleRegular;
    }

    public void setFuelSaleRegular(double fuelSaleRegular) {
        this.fuelSaleRegular = fuelSaleRegular;
    }

    public double getFuelSalesMidGrade() {
        return fuelSalesMidGrade;
    }

    public void setFuelSalesMidGrade(double fuelSalesMidGrade) {
        this.fuelSalesMidGrade = fuelSalesMidGrade;
    }

    public double getFuelSalesPremium() {
        return fuelSalesPremium;
    }

    public void setFuelSalesPremium(double fuelSalesPremium) {
        this.fuelSalesPremium = fuelSalesPremium;
    }

    public double getFuelSalesDiesel() {
        return fuelSalesDiesel;
    }

    public void setFuelSalesDiesel(double fuelSalesDiesel) {
        this.fuelSalesDiesel = fuelSalesDiesel;
    }

    public double getConvenienceStoreSales() {
        return convenienceStoreSales;
    }

    public void setConvenienceStoreSales(double convenienceStoreSales) {
        this.convenienceStoreSales = convenienceStoreSales;
    }

    public double getRestaurantSales() {
        return restaurantSales;
    }

    public void setRestaurantSales(double restaurantSales) {
        this.restaurantSales = restaurantSales;
    }

    public double getTobaccoSales() {
        return tobaccoSales;
    }

    public void setTobaccoSales(double tobaccoSales) {
        this.tobaccoSales = tobaccoSales;
    }
}

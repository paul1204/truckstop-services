package com.truckstopservices.inventory.fuel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FuelDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fuelDeliveryID;
    private String date;
    private double dieselQtyOrdered;
    private double dieselPricePerGallon;
    private double regularOctaneQtyOrdered;
    private double regularOctanePricePerGallon;
    private double premiumOctaneQtyOrdered;
    private double premiumOctanePricePerGallon;

    private String companyName;

    //Mid Grade is a mixture of Premium and Regular Octane. No Mid Grade fuel order

    public FuelDelivery(String fuelDeliveryID, double dieselQtyOrdered, double dieselPricePerGallon, double regularOctaneQtyOrdered,
                        double regularOctanePricePerGallon, double premiumOctaneQtyOrdered, double premiumOctanePricePerGallon, String companyName) {
        this.fuelDeliveryID = fuelDeliveryID;
        this.dieselQtyOrdered = dieselQtyOrdered;
        this.dieselPricePerGallon = dieselPricePerGallon;
        this.regularOctaneQtyOrdered = regularOctaneQtyOrdered;
        this.regularOctanePricePerGallon = regularOctanePricePerGallon;
        this.premiumOctaneQtyOrdered = premiumOctaneQtyOrdered;
        this.premiumOctanePricePerGallon = premiumOctanePricePerGallon;
        this.companyName = companyName;
    }

    public String getFuelDeliveryID() {
        return fuelDeliveryID;
    }

    public void setFuelDeliveryID(String fuelDeliveryID) {
        this.fuelDeliveryID = fuelDeliveryID;
    }

    public double getDieselQtyOrdered() {
        return dieselQtyOrdered;
    }

    public void setDieselQtyOrdered(double dieselQtyOrdered) {
        this.dieselQtyOrdered = dieselQtyOrdered;
    }

    public double getDieselPricePerGallon() {
        return dieselPricePerGallon;
    }

    public void setDieselPricePerGallon(double dieselPricePerGallon) {
        this.dieselPricePerGallon = dieselPricePerGallon;
    }

    public double getRegularOctaneQtyOrdered() {
        return regularOctaneQtyOrdered;
    }

    public void setRegularOctaneQtyOrdered(double regularOctaneQtyOrdered) {
        this.regularOctaneQtyOrdered = regularOctaneQtyOrdered;
    }

    public double getRegularOctanePricePerGallon() {
        return regularOctanePricePerGallon;
    }

    public void setRegularOctanePricePerGallon(double regularOctanePricePerGallon) {
        this.regularOctanePricePerGallon = regularOctanePricePerGallon;
    }

    public double getPremiumOctaneQtyOrdered() {
        return premiumOctaneQtyOrdered;
    }

    public void setPremiumOctaneQtyOrdered(double premiumOctaneQtyOrdered) {
        this.premiumOctaneQtyOrdered = premiumOctaneQtyOrdered;
    }

    public double getPremiumOctanePricePerGallon() {
        return premiumOctanePricePerGallon;
    }

    public void setPremiumOctanePricePerGallon(double premiumOctanePricePerGallon) {
        this.premiumOctanePricePerGallon = premiumOctanePricePerGallon;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double returnDeliveryAmount(){
        return (this.dieselPricePerGallon + this.dieselQtyOrdered) +
                (this.regularOctanePricePerGallon + this.regularOctaneQtyOrdered) +
                (this.premiumOctanePricePerGallon + this.premiumOctaneQtyOrdered);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

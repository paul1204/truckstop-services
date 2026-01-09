package com.truckstopservices.inventory.fuel.model;


import jakarta.persistence.*;

import java.util.List;

@MappedSuperclass
public abstract class Fuel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long delivery_id;
    private int octane;
    String deliveryDate;
    private double pricePerGallon;

    private double totalGallons;

    private double availableGallons;

   // private double averagePrice;

    private Long nextDelivery_id;

    private boolean active = true;

    public Fuel(){
    }

    public Fuel(String deliveryDate, int octane, double pricePerGallon, double initalFuelGallons){
        this.deliveryDate = deliveryDate;
        this.octane = octane;
        this.pricePerGallon = pricePerGallon;
        this.totalGallons = initalFuelGallons;
        this.availableGallons = initalFuelGallons;
    }

    public Long getDelivery_id() {
        return delivery_id;
    }

    public int getOctane() {
        return octane;
    }

    public void setOctane(int octane) {
        this.octane = octane;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String date) {
        this.deliveryDate = date;
    }

    public double getPricePerGallon() {
        return pricePerGallon;
    }

    public void setPricePerGallon(double pricePerGallon) {
        this.pricePerGallon = pricePerGallon;
    }

    public double calculateTotalPrice(double quantity){
        return getPricePerGallon() * quantity;
    };

    public double getTotalGallons() {
        return totalGallons;
    }

    public void setTotalGallons(double totalGallons) {
        this.totalGallons = totalGallons;
    }

    public double getAvailableGallons() {
        return availableGallons;
    }

    public void setAvailableGallons(double initalFuelGallons) {
        this.availableGallons = initalFuelGallons;
    }

    public void updateGallonsReduceInventorySales(double gallonsSold){
        this.availableGallons -= gallonsSold;
    }

    public void updateGallonsAddInventory(double gallonsDelivered){this.availableGallons += gallonsDelivered;}

//    public double getAveragePrice() {
//        return averagePrice;
//    }
//
//    public void setAveragePrice(double averagePrice) {
//        this.averagePrice = averagePrice;
//    }

    public Long getNextDelivery_id() {
        return nextDelivery_id;
    }

    public void setNextDelivery_id(Long nextDelivery_id) {
        this.nextDelivery_id = nextDelivery_id;
    }

    public void setFlagInactive(){
        this.active = false;
    }

}

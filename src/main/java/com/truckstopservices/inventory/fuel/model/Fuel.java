package com.truckstopservices.inventory.fuel.model;


import jakarta.persistence.*;

@MappedSuperclass
public abstract class Fuel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long delivery_id;
    private int octane;
    String deliveryDate;
    private double costPerGallon;
    private double retailPrice;
    private double totalGallons;
    private double availableGallons;

   // private double averagePrice;

    private Long nextDelivery_id;

    private boolean active = true;

    public Fuel(){
    }

    public Fuel(String deliveryDate, int octane, double costPerGallon, double initalFuelGallons){
        this.deliveryDate = deliveryDate;
        this.octane = octane;
        this.costPerGallon = costPerGallon;
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

    public double getCostPerGallon() {
        return costPerGallon;
    }

    public void setCostPerGallon(double costPerGallon) {
        this.costPerGallon = costPerGallon;
    }

    public double calculateTotalPrice(double quantity){
        return getCostPerGallon() * quantity;
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
    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

}

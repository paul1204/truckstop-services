package com.truckstopservices.inventory.fuel.model;


import com.truckstopservices.processing.entity.FuelSales;
import jakarta.persistence.*;

import java.util.List;
import java.util.Queue;

@MappedSuperclass
public abstract class Fuel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int octane;
    private double pricePerGallon;

    private double totalGallons;

    private double availableGallons;

    private List<Double> fifoPrice;
    private List<Double> fifoGallons;

    private double averagePrice;

    public Fuel(){
    }

    public Fuel(int octane, double pricePerGallon, double initalFuelGallons, List<Double> fifoPrice, List<Double> fifoGallons){
        this.octane = octane;
        this.pricePerGallon = pricePerGallon;
        this.totalGallons = initalFuelGallons;
        this.availableGallons = initalFuelGallons;
        this.fifoPrice = fifoPrice;
        this.fifoGallons = fifoGallons;
        this.averagePrice = pricePerGallon;
    }

    public int getOctane() {
        return octane;
    }

    public void setOctane(int octane) {
        this.octane = octane;
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

    public void setAvailableGallons(double availableGallons) {
        this.availableGallons = availableGallons;
    }

    public void updateGallonsReduceInventorySales(double gallonsSold){
        this.availableGallons -= gallonsSold;
    }

    public void updateGallonsAddInventory(double gallonsDelivered){this.availableGallons += gallonsDelivered;}

    public List<Double> getFifoPrice() {
        return fifoPrice;
    }

    public void setFifoPrice(List<Double> fifoPrice) {
        this.fifoPrice = fifoPrice;
    }

    public List<Double> getFifoGallons() {
        return fifoGallons;
    }

    public void setFifoGallons(List<Double> fifoGallons) {
        this.fifoGallons = fifoGallons;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }
}

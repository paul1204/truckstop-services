package com.truckstopservices.inventory.fuel.model;


import com.truckstopservices.processing.entity.FuelSales;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class FuelModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int octane;
    private double pricePerGallon;

    private double totalGallons;

    private double availableGallons;

    public FuelModel(){
    }

    public FuelModel(int octane, double pricePerGallon, double initalFuel){
        this.octane = octane;
        this.pricePerGallon = pricePerGallon;
        this.totalGallons = initalFuel;
        this.availableGallons = initalFuel;
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

    public void setAvailableGallons(int availableGallons) {
        this.availableGallons = availableGallons;
    }

    public void updateGallonsReduceInventory(double gallonsSold){
        this.availableGallons -= gallonsSold;
    }
}

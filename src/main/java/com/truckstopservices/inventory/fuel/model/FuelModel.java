package com.truckstopservices.inventory.fuel.model;

public abstract class FuelModel {
    private int octane;
    private double pricePerGallon;

    public FuelModel(int octane, double pricePerGallon){
        this.octane = octane;
        this.pricePerGallon = pricePerGallon;
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

    public abstract double calculateTotalPrice(double quantity);

}

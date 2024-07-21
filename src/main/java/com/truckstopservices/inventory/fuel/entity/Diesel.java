package com.truckstopservices.inventory.fuel.entity;

import com.truckstopservices.inventory.fuel.model.CalculateFuelPrice;
import com.truckstopservices.inventory.fuel.model.FuelModel;

public class Diesel extends FuelModel implements CalculateFuelPrice {
    public Diesel(int octane, double pricePerGallon) {
        super(octane, pricePerGallon);
    }


    @Override
    public double calculateGallonsToDispense(double amount) {
        return amount / getPricePerGallon();
    }
}

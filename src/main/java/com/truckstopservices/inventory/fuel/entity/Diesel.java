package com.truckstopservices.inventory.fuel.entity;

import com.truckstopservices.inventory.fuel.model.CalculateFuelPrice;
import com.truckstopservices.inventory.fuel.model.Fuel;
import jakarta.persistence.Entity;

@Entity
public class Diesel extends Fuel implements CalculateFuelPrice {

    public Diesel(){
        super();
    }
    public Diesel(String deliveryDate, int octane, double costPerGallon, double initalGallons) {
        super(deliveryDate, octane, costPerGallon, initalGallons);
    }
    @Override
    public double calculateGallonsToDispense(double amount) {
        return amount / getCostPerGallon();
    }
}

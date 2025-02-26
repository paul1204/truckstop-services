package com.truckstopservices.inventory.fuel.entity;

import com.truckstopservices.inventory.fuel.model.CalculateFuelPrice;
import com.truckstopservices.inventory.fuel.model.Fuel;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PremiumOctane extends Fuel implements CalculateFuelPrice {

    public PremiumOctane(){
        super();
    }
    public PremiumOctane(String deliveryDate , int octane, double pricePerGallon, double initalGallons) {
        super(deliveryDate, octane,pricePerGallon, initalGallons);
    }
    @Override
    public double calculateGallonsToDispense(double amount) {
        return amount / getPricePerGallon();
    }
}

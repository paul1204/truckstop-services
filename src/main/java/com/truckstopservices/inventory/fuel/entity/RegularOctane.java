package com.truckstopservices.inventory.fuel.entity;

import com.truckstopservices.inventory.fuel.model.CalculateFuelPrice;
import com.truckstopservices.inventory.fuel.model.Fuel;
import com.truckstopservices.inventory.fuel.model.Fuel;
import jakarta.persistence.Entity;


@Entity
public class RegularOctane extends Fuel implements CalculateFuelPrice {

    public RegularOctane(){
        super();
    }
    public RegularOctane(int octane, double pricePerGallon, double initalGallons) {
        super(octane, pricePerGallon, initalGallons);
    }

    @Override
    public double calculateGallonsToDispense(double amount) {
        return amount / getPricePerGallon();
    }
}

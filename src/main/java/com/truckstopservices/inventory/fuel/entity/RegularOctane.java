package com.truckstopservices.inventory.fuel.entity;

import com.truckstopservices.inventory.fuel.model.CalculateFuelPrice;
import com.truckstopservices.inventory.fuel.model.FuelModel;
import jakarta.persistence.Entity;


@Entity
public class RegularOctane extends FuelModel implements CalculateFuelPrice {

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

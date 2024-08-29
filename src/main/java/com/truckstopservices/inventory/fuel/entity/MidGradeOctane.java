package com.truckstopservices.inventory.fuel.entity;

import com.truckstopservices.inventory.fuel.model.CalculateFuelPrice;
import com.truckstopservices.inventory.fuel.model.Fuel;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;


@Entity
public class MidGradeOctane extends Fuel implements CalculateFuelPrice {

    public MidGradeOctane(){
        super();
    }
    public MidGradeOctane(int octane, double pricePerGallon, double initalGallons) {
        super(octane, pricePerGallon, initalGallons, new ArrayList<Double>(List.of(pricePerGallon)), new ArrayList<Double>(List.of(initalGallons)));
    }

    @Override
    public double calculateGallonsToDispense(double amount) {
        return amount / getPricePerGallon();
    }
}

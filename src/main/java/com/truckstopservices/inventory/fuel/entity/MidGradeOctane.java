package com.truckstopservices.inventory.fuel.entity;

import com.truckstopservices.inventory.fuel.model.CalculateFuelPrice;
import com.truckstopservices.inventory.fuel.model.Fuel;
import jakarta.persistence.Entity;


@Entity
public class MidGradeOctane extends Fuel implements CalculateFuelPrice {

    public MidGradeOctane(){
        super();
    }
//    public MidGradeOctane(int octane, double costPerGallon, double initalGallons) {
//        super(octane, costPerGallon, initalGallons, new ArrayList<Double>(List.of(costPerGallon)), new ArrayList<Double>(List.of(initalGallons)));
//    }

    @Override
    public double calculateGallonsToDispense(double amount) {
        return amount / getCostPerGallon();
    }
}

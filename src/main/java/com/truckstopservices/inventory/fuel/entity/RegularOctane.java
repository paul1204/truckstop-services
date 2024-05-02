package com.truckstopservices.inventory.fuel.entity;

import com.truckstopservices.inventory.fuel.model.FuelModel;

public class RegularOctane extends FuelModel {
    public RegularOctane(int octane, double pricePerGallon) {
        super(octane, pricePerGallon);
    }

    @Override
    public double calculateTotalPrice(double quantity) {
        return 0;
    }
}

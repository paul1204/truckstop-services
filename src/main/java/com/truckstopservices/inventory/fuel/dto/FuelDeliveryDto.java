package com.truckstopservices.inventory.fuel.dto;

public record FuelDeliveryDto (
        String fuelDeliveryId,
        String date,
        double dieselQtyOrdered,
        double dieselOrderedCostPerGallon,
        double regularOctaneQtyOrdered,
        double regularOctaneCostPerGallon,
        double premiumOctaneQtyOrdered,
        double premiumOctaneCostPerGallon
){
}

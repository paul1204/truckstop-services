package com.truckstopservices.inventory.fuel.dto;

public record FuelDeliveryDto (
        String fuelDeliveryId,
        String date,
        double dieselQtyOrdered,
        double dieselOrderedPricePerGallon,
        double regularOctaneQtyOrdered,
        double regularOctanePricePerGallon,
        double premiumOctaneQtyOrdered,
        double premiumOctanePricePerGallon
){
}

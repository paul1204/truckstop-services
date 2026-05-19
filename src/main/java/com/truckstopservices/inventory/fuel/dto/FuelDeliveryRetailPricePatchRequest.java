package com.truckstopservices.inventory.fuel.dto;

public record FuelDeliveryRetailPricePatchRequest(
        Long id,
        Double dieselRetailPrice,
        Double regularRetailPrice,
        Double premiumRetailPrice
) {
}
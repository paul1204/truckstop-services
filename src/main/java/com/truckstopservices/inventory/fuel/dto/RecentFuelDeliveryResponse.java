package com.truckstopservices.inventory.fuel.dto;

public record RecentFuelDeliveryResponse(
        Long id,
        String companyName,
        String fuelDeliveryId,
        String deliveryDate,
        Double dieselGallons,
        Double dieselRetailPrice,
        Double regularGallons,
        Double regularRetailPrice,
        Double premiumGallons,
        Double premiumRetailPrice
) {
}
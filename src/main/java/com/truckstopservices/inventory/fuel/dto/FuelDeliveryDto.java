package com.truckstopservices.inventory.fuel.dto;

public record FuelDeliveryDto (
        String companyName,
        String fuelDeliveryId,
        String deliveryDate,
        FuelOrderDto dieselOrder,
        FuelOrderDto regularOctaneOrder,
        FuelOrderDto premiumOctaneOrder
){
}

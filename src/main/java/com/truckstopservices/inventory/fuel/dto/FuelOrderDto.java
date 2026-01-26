package com.truckstopservices.inventory.fuel.dto;

public record FuelOrderDto(
        int octane,
        Double pricePerGallon,
        Double totalGallons
) {}

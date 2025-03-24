package com.truckstopservices.inventory.fuel.dto;

public record FuelSaleRequest(
        int octane,
        double gallonsSold,
        double totalPrice,
        String specialMessage
) {
}

package com.truckstopservices.inventory.fuel.dto;

public record FuelSaleRequest(
        String octane,
        double gallonsSold,
        String totalPrice
) {
}

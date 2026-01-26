package com.truckstopservices.inventory.fuel.dto;

public record FuelSaleRequest(
        int octane,
        Double gallonsSold,
        Double totalPrice,
        String specialMessage,
        String terminal
) {
}

package com.truckstopservices.inventory.fuel.dto;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccountTransaction;

public record FuelSaleHouseAccountResponse(
    int octane,
    double gallonsSold,
    double totalPrice,
    String specialMessage,
    HouseAccountTransaction houseAccountTransaction
) {
    // Constructor to create from FuelSaleRequest and HouseAccountTransaction
    public static FuelSaleHouseAccountResponse fromFuelSaleRequestAndHouseAccountTransaction(
            FuelSaleRequest request, 
            HouseAccountTransaction houseAccountTransaction) {
        return new FuelSaleHouseAccountResponse(
            request.octane(),
            request.gallonsSold(),
            request.totalPrice(),
            request.specialMessage(),
            houseAccountTransaction
        );
    }
}
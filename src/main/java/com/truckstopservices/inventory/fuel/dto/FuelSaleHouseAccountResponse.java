package com.truckstopservices.inventory.fuel.dto;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccountTransaction;
import com.truckstopservices.accounting.sales.receipt.Receipt;

public record FuelSaleHouseAccountResponse(
    int octane,
    double gallonsSold,
    double totalPrice,
    String specialMessage,
    HouseAccountTransaction houseAccountTransaction,
    Receipt receipt
) {
    // Constructor to create from FuelSaleRequest and HouseAccountTransaction
    public static FuelSaleHouseAccountResponse fromFuelSaleRequestAndHouseAccountTransaction(
            FuelSaleRequest request, 
            HouseAccountTransaction houseAccountTransaction, Receipt receipt) {
        return new FuelSaleHouseAccountResponse(
            request.octane(),
            request.gallonsSold(),
            request.totalPrice(),
            request.specialMessage(),
            houseAccountTransaction, receipt
        );
    }
}
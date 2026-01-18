package com.truckstopservices.inventory.fuel.dto;

import com.truckstopservices.accounting.sales.receipt.Receipt;

public record FuelSaleResponse(
    int octane,
    double gallonsSold,
    double totalPrice,
    String specialMessage,
    String terminal,
    Receipt receipt
) {
    // Constructor to create from FuelSaleRequest and Receipt
    public static FuelSaleResponse fromFuelSaleRequestAndReceipt(FuelSaleRequest request, Receipt receipt) {
        return new FuelSaleResponse(
            request.octane(),
            request.gallonsSold(),
            request.totalPrice(),
            request.specialMessage(),
            request.terminal(),
            receipt
        );
    }
}
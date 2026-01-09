package com.truckstopservices.accounting.sales.receipt;
//
//public sealed interface SaleDetails permits FuelDetails, MerchandiseDetails {}
//
//public record FuelDetails(String type, double amount, double price) implements SaleDetails {}
//public record MerchandiseDetails(List<String> items, double totalWeight) implements SaleDetails {}

import com.truckstopservices.common.types.SalesType;

public record Receipt(
        String receiptId,
        String salesId,
        SalesType salesType,
        Double totalAmount
        //List of Items.
) {

}

package com.truckstopservices.processing.dto;

public record ShiftReportDto(
        String date,
        String shiftNumber,
        String employeeID,
        String managerID,
        double posCashTil1,
        double posCashTil2,
        Double[] fuelSales,
        double merchandiseSales,
        double restaurantSales,
        double tobaccoSales,
        double nonRestaurantSales,
        double bottledBeverageSales
) {}

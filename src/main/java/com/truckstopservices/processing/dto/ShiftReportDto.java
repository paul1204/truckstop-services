package com.truckstopservices.processing.dto;

public record ShiftReportDto(
        String date,
        String shiftNumber,
        String employeeID,
        String managerID,
        double posCashTil1,
        double posCashTil2,
        double fuelSaleRegular,
        double fuelSalesMidGrade,
        double fuelSalesPremium,
        double fuelSalesDiesel,
        double merchandiseSales,
        double restaurantSales,
        double tobaccoSales,
        double nonRestaurantSales,
        double bottledBeverageSales
) {}

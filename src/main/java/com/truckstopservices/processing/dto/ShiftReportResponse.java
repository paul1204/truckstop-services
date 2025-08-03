package com.truckstopservices.processing.dto;

import com.truckstopservices.processing.entity.ShiftReport;
import java.util.List;

public record ShiftReportResponse(
    String date,
    String shiftNumber,
    String employeeID,
    String managerID,
    double posCashTil1,
    double posCashTil2,
    List<Double> fuelSales,
    double merchandiseSales,
    double restaurantSales,
    double tobaccoSales,
    double nonRestaurantSales,
    double bottledBeverageSales
) {
    public static ShiftReportResponse fromEntity(ShiftReport shiftReport) {
        List<Double> fuelSalesList = null;
        double merchandiseSalesValue = 0.0;
        double restaurantSalesValue = 0.0;
        double tobaccoSalesValue = 0.0;
        double nonRestaurantSalesValue = 0.0;
        double bottledBeverageSalesValue = 0.0;
        
        if (shiftReport.getFuelSales() != null) {
            fuelSalesList = List.of(
                shiftReport.getFuelSales().getRegularGasolineTransactions(),
                shiftReport.getFuelSales().getMidGradeGasolineTransactions(),
                shiftReport.getFuelSales().getPremiumGasolineTransactions(),
                shiftReport.getFuelSales().getDieselTransactions()
            );
        }
        
        if (shiftReport.getMerchandiseSales() != null) {
            merchandiseSalesValue = shiftReport.getMerchandiseSales().getMerchandiseSales();
            nonRestaurantSalesValue = shiftReport.getMerchandiseSales().getNonRestaurantSales();
            bottledBeverageSalesValue = shiftReport.getMerchandiseSales().getBottledBeverageSales();
        }
        
        if (shiftReport.getRestaurantSales() != null) {
            restaurantSalesValue = shiftReport.getRestaurantSales().getRestaurantSalesSales();
        }
        
        if (shiftReport.getTobaccoSales() != null) {
            tobaccoSalesValue = shiftReport.getTobaccoSales().getTobaccoSales();
        }
        
        return new ShiftReportResponse(
            shiftReport.getDate(),
            shiftReport.getShiftNumber(),
            shiftReport.getEmployeeID(),
            shiftReport.getManagerID(),
            shiftReport.getPosCashTil1(),
            shiftReport.getPosCashTil2(),
            fuelSalesList,
            merchandiseSalesValue,
            restaurantSalesValue,
            tobaccoSalesValue,
            nonRestaurantSalesValue,
            bottledBeverageSalesValue
        );
    }
}
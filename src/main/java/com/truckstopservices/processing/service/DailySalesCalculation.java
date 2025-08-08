//package com.truckstopservices.processing.service;
//
//import com.truckstopservices.processing.dto.DailySalesDto;
//import com.truckstopservices.processing.entity.ShiftReport;
//import com.truckstopservices.processing.repository.ShiftReportRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class DailySalesCalculation {
//
//    @Autowired
//    private ShiftReportRepository shiftReportRepository;
//
//    public DailySalesDto getDailySales(String date){
//        List<ShiftReport> shiftReports = shiftReportRepository.findByDate(date);
//
//        double totalFuelSales = shiftReports.stream()
//                .mapToDouble(shift -> shift.getFuelSales().getRegularGasolineTransactions() +
//                        shift.getFuelSales().getMidGradeGasolineTransactions() +
//                        shift.getFuelSales().getPremiumGasolineTransactions() +
//                        shift.getFuelSales().getDieselTransactions())
//                .sum();
//
//        double totalMerchandiseSales = shiftReports.stream()
//                .mapToDouble(shift -> shift.getMerchandiseSales().getMerchandiseSales())
//                .sum();
//
//        double totalRestaurantSales = shiftReports.stream()
//                .mapToDouble(s -> s.getRestaurantSales().getRestaurantSalesSales())
//                .sum();
//        double totalTobaccoSales = shiftReports.stream()
//                .mapToDouble(s -> s.getTobaccoSales().getTobaccoSales())
//                .sum();
//        return new DailySalesDto(date, totalFuelSales, totalMerchandiseSales, totalRestaurantSales, totalTobaccoSales);
//    }
//}

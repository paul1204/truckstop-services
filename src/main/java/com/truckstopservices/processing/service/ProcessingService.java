package com.truckstopservices.processing.service;

import com.truckstopservices.processing.dto.DailySalesDto;
import com.truckstopservices.processing.entity.*;
import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.repository.ShiftReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProcessingService {
    @Autowired
    private ShiftReportRepository shiftReportRepository;

    @Transactional
    public ShiftReport parseShiftData(ShiftReportDto shiftReportDto){
        ShiftReport shiftReport = shiftReportRepository.findByShiftNumber(shiftReportDto.shiftNumber())
                .orElse(new ShiftReport());
        shiftReport.setDate(shiftReportDto.date());
        shiftReport.setShiftNumber(shiftReportDto.shiftNumber());
        shiftReport.setEmployeeID(shiftReportDto.employeeID());
        shiftReport.setManagerID(shiftReportDto.managerID());
        shiftReport.setPosCashTil1(shiftReportDto.posCashTil1());
        shiftReport.setPosCashTil2(shiftReportDto.posCashTil2());

        //Set<FuelSales> fuelSalesSet = new HashSet<>();
        FuelSales fuelSale = new FuelSales();
        fuelSale.setRegularGasolineTransactions(shiftReportDto.fuelSaleRegular());
        fuelSale.setMidGradeGasolineTransactions(shiftReportDto.fuelSalesMidGrade());
        fuelSale.setMidGradeGasolineTransactions(shiftReportDto.fuelSalesPremium());
        fuelSale.setMidGradeGasolineTransactions(shiftReportDto.fuelSalesDiesel());

        MerchandiseSales merchandiseSales = new MerchandiseSales();
        merchandiseSales.setMerchandiseSales(shiftReportDto.merchandiseSales());

        RestaurantSales restaurantSales = new RestaurantSales();
        restaurantSales.setTotalRestaurantSalesSales(shiftReportDto.restaurantSales());

        TobaccoSales tobaccoSales = new TobaccoSales();
        tobaccoSales.setTobaccoSales(shiftReportDto.tobaccoSales());

        fuelSale.setShiftReport(shiftReport);
        merchandiseSales.setShiftReport(shiftReport);
        restaurantSales.setShiftReport(shiftReport);
        tobaccoSales.setShiftReport(shiftReport);
        
        return shiftReport;
    }

    public void saveToRepository(ShiftReport shiftReport){
        shiftReportRepository.save(shiftReport);
    }

    public void pushToAccountingService(ShiftReport posReport){
        //make API call to Revenue Management Controller
    }

    public DailySalesDto getDailySales(String date){
        List<ShiftReport> shiftReports = shiftReportRepository.findByDate(date);

        double totalFuelSales = shiftReports.stream()
                .mapToDouble(shift -> shift.getFuelSales().getRegularGasolineTransactions() +
                        shift.getFuelSales().getMidGradeGasolineTransactions() +
                        shift.getFuelSales().getPremiumGasolineTransactions() +
                        shift.getFuelSales().getDieselTransactions())
                .sum();

        double totalMerchandiseSales = shiftReports.stream()
                .mapToDouble(shift -> shift.getMerchandiseSales().getMerchandiseSales())
                .sum();

        double totalRestaurantSales = shiftReports.stream()
                .mapToDouble(s -> s.getRestaurantSales().getRestaurantSalesSales())
                .sum();
        double totalTobaccoSales = shiftReports.stream()
                .mapToDouble(s -> s.getTobaccoSales().getTobaccoSales())
                .sum();
        return new DailySalesDto(date, totalFuelSales, totalMerchandiseSales, totalRestaurantSales, totalTobaccoSales);
    }

}

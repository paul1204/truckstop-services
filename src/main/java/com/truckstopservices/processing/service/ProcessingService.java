package com.truckstopservices.processing.service;

import com.truckstopservices.inventory.fuel.model.FuelModel;
import com.truckstopservices.inventory.fuel.service.FuelService;
import com.truckstopservices.processing.dto.DailySalesDto;
import com.truckstopservices.processing.entity.*;
import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.repository.ShiftReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProcessingService {
    @Autowired
    private ShiftReportRepository shiftReportRepository;

    @Autowired
    private FuelService fuelService;

    public ShiftReportDto parsePOSFile(String rawDtoString){
        String[] lines = rawDtoString.split("\n");
        System.out.println(rawDtoString);
        Map<String, String> dtopMap = new HashMap<>();

        for(String line : lines){
            String[] data = line.split(":",2);
            if(data.length == 2){
                dtopMap.put(data[0].trim(), data[1].trim());
            }
        }
        String date = dtopMap.get("DATE");
        String shiftNumber = dtopMap.get("SHIFT_NUMBER");
        String employeeID = dtopMap.get("EMPLOYEE_ID");
        String managerID = dtopMap.get("MANAGER_ID");

        double posCashTil1 = Double.parseDouble(dtopMap.get("STARTING_DRAWER_POS1").replaceAll("[$,]", ""));
        double posCashTil2 = Double.parseDouble(dtopMap.get("STARTING_DRAWER_POS2").replaceAll("[$,]", ""));

        double fuelSaleRegular = Double.parseDouble(dtopMap.get("REGULAR_GASOLINE_TRANSACTIONS").replaceAll("[$,]", ""));
        double fuelSalesMidGrade = Double.parseDouble(dtopMap.get("MID_GRADE_GASOLINE_TRANSACTIONS").replaceAll("[$,]", ""));
        double fuelSalesPremium = Double.parseDouble(dtopMap.get("PREMIUM_GASOLINE_TRANSACTIONS").replaceAll("[$,]", ""));
        double fuelSalesDiesel = Double.parseDouble(dtopMap.get("DIESEL_TRANSACTIONS").replaceAll("[$,]", ""));

        double merchandiseSales = Double.parseDouble(dtopMap.get("TOTAL_CONVENIENCE_STORE_SALES").replaceAll("[$,]", ""));
        double nonRestaurantSales = Double.parseDouble(dtopMap.get("NON_RESTAURANT_FOOD").replaceAll("[$,]]", ""));
        double bottledBeverage = Double.parseDouble(dtopMap.get("BOTTLED_BEVERAGES").replaceAll("[$,]]", ""));
        double restaurantSales = Double.parseDouble(dtopMap.get("TOTAL_RESTAURANT_SALES").replaceAll("[$,]", ""));
        double tobaccoSale = Double.parseDouble(dtopMap.get("TOTAL_TOBACCO_SALES").replaceAll("[$,]", ""));
        return new ShiftReportDto(date, shiftNumber, employeeID, managerID, posCashTil1, posCashTil2,
                fuelSaleRegular, fuelSalesMidGrade, fuelSalesPremium, fuelSalesDiesel,
                merchandiseSales, restaurantSales, tobaccoSale, nonRestaurantSales, bottledBeverage);
    }
    @Transactional
    public void parseShiftData(ShiftReportDto shiftReportDto){
        ShiftReport s = createShiftReport(shiftReportDto);
        saveToRepository(s);

        //update inventory
        fuelService.updateFuelInventoryDeductAvailableGallonsFromSales(shiftReportDto);
    }

    private ShiftReport createShiftReport(ShiftReportDto shiftReportDto){
        ShiftReport shiftReport = shiftReportRepository.findByShiftNumber(shiftReportDto.shiftNumber())
                .orElse(new ShiftReport());
        shiftReport.setDate(shiftReportDto.date());
        shiftReport.setShiftNumber(shiftReportDto.shiftNumber());
        shiftReport.setEmployeeID(shiftReportDto.employeeID());
        shiftReport.setManagerID(shiftReportDto.managerID());
        shiftReport.setPosCashTil1(shiftReportDto.posCashTil1());
        shiftReport.setPosCashTil2(shiftReportDto.posCashTil2());

        FuelSales fuelSale = new FuelSales();
        fuelSale.setRegularGasolineTransactions(shiftReportDto.fuelSaleRegular());
        fuelSale.setMidGradeGasolineTransactions(shiftReportDto.fuelSalesMidGrade());
        fuelSale.setPremiumGasolineTransactions(shiftReportDto.fuelSalesPremium());
        fuelSale.setDieselTransactions(shiftReportDto.fuelSalesDiesel());

        MerchandiseSales merchandiseSales = new MerchandiseSales();
        merchandiseSales.setMerchandiseSales(shiftReportDto.merchandiseSales());
        merchandiseSales.setNonRestaurantSales(shiftReportDto.nonRestaurantSales());
        merchandiseSales.setBottledBeverageSales(shiftReportDto.bottledBeverageSales());

        RestaurantSales restaurantSales = new RestaurantSales();
        restaurantSales.setTotalRestaurantSalesSales(shiftReportDto.restaurantSales());

        TobaccoSales tobaccoSales = new TobaccoSales();
        tobaccoSales.setTobaccoSales(shiftReportDto.tobaccoSales());

        fuelSale.setShiftReport(shiftReport);
        merchandiseSales.setShiftReport(shiftReport);
        restaurantSales.setShiftReport(shiftReport);
        tobaccoSales.setShiftReport(shiftReport);

        // Save the entities
        shiftReport.setFuelSales(fuelSale);
        shiftReport.setMerchandiseSales(merchandiseSales);
        shiftReport.setRestaurantSales(restaurantSales);
        shiftReport.setTobaccoSales(tobaccoSales);

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

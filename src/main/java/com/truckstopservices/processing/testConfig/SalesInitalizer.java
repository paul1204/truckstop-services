package com.truckstopservices.processing.testConfig;

import com.truckstopservices.processing.entity.*;
import com.truckstopservices.processing.repository.ShiftReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SalesInitalizer {

    @Autowired
    private ShiftReportRepository shiftReportRepository;


    @Bean
    public CommandLineRunner loadData(ShiftReportRepository shiftReportRepository) {
        return args -> {
            // Shift 1
            FuelSales fuelSales1 = new FuelSales(100.00);
            MerchandiseSales merchandiseSales1 = new MerchandiseSales(200.00);
            RestaurantSales restaurantSales1 = new RestaurantSales(50.00);
            TobaccoSales tobaccoSales1 = new TobaccoSales(50.00);

            ShiftReport shiftReport1 = new ShiftReport("7/26/2024", "1", "123", "987",
                    100.00, 100.00, fuelSales1, merchandiseSales1, restaurantSales1, tobaccoSales1);

            fuelSales1.setShiftReport(shiftReport1);
            merchandiseSales1.setShiftReport(shiftReport1);
            restaurantSales1.setShiftReport(shiftReport1);
            tobaccoSales1.setShiftReport(shiftReport1);

            // Save Shift 1
            shiftReportRepository.save(shiftReport1);

            // Shift 2
            FuelSales fuelSales2 = new FuelSales(150.00);
            MerchandiseSales merchandiseSales2 = new MerchandiseSales(250.00);
            RestaurantSales restaurantSales2 = new RestaurantSales(60.00);
            TobaccoSales tobaccoSales2 = new TobaccoSales(60.00);

            ShiftReport shiftReport2 = new ShiftReport("7/27/2024", "2", "124", "988",
                    150.00, 150.00, fuelSales2, merchandiseSales2, restaurantSales2, tobaccoSales2);

            fuelSales2.setShiftReport(shiftReport2);
            merchandiseSales2.setShiftReport(shiftReport2);
            restaurantSales2.setShiftReport(shiftReport2);
            tobaccoSales2.setShiftReport(shiftReport2);

            // Save Shift 2
            shiftReportRepository.save(shiftReport2);

            // Shift 3
            FuelSales fuelSales3 = new FuelSales(200.00);
            MerchandiseSales merchandiseSales3 = new MerchandiseSales(300.00);
            RestaurantSales restaurantSales3 = new RestaurantSales(70.00);
            TobaccoSales tobaccoSales3 = new TobaccoSales(70.00);

            ShiftReport shiftReport3 = new ShiftReport("7/28/2024", "3", "125", "989",
                    200.00, 200.00, fuelSales3, merchandiseSales3, restaurantSales3, tobaccoSales3);

            fuelSales3.setShiftReport(shiftReport3);
            merchandiseSales3.setShiftReport(shiftReport3);
            restaurantSales3.setShiftReport(shiftReport3);
            tobaccoSales3.setShiftReport(shiftReport3);

            // Save Shift 3
            shiftReportRepository.save(shiftReport3);
        };
    }


}

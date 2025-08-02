package com.truckstopservices.processing.service;

import com.truckstopservices.inventory.fuel.service.FuelService;
import com.truckstopservices.inventory.merchandise.repository.BeverageRepository;
import com.truckstopservices.inventory.merchandise.service.MerchandiseService;
import com.truckstopservices.processing.client.MerchandiseManager;
import com.truckstopservices.processing.dto.InventoryDto;
import com.truckstopservices.processing.entity.*;
import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.repository.ShiftReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ProcessingService {
    @Autowired
    private ShiftReportRepository shiftReportRepository;

    @Autowired
    private FuelService fuelService;

    @Autowired
    private MerchandiseService merchandiseService;

    @Autowired
    private BeverageRepository beverageRepository;

    @Autowired
    private final MerchandiseManager merchandiseManagerClient;

    public ProcessingService(BeverageRepository beverageRepository, MerchandiseManager merchandiseManagerClient) {
        this.beverageRepository = beverageRepository;
        this.merchandiseManagerClient = merchandiseManagerClient;
    }

    public ShiftReportDto parsePOSShiftFile(String rawDtoString) {
        String[] lines = rawDtoString.split("\n");
        System.out.println(rawDtoString);
        Map<String, String> dtopMap = new HashMap<>();

        for (String line : lines) {
            String[] data = line.split(":", 2);
            if (data.length == 2) {
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
        double restaurantSales = Double.parseDouble(dtopMap.get("TOTAL_RESTAURANT_SALES").replaceAll("[$,]", ""));
        double tobaccoSale = Double.parseDouble(dtopMap.get("TOTAL_TOBACCO_SALES").replaceAll("[$,]", ""));

        return new ShiftReportDto(date, shiftNumber, employeeID, managerID, posCashTil1, posCashTil2,
                // fuelSaleRegular, fuelSalesMidGrade, fuelSalesPremium, fuelSalesDiesel,
                new Double[]{fuelSaleRegular, fuelSalesMidGrade, fuelSalesPremium, fuelSalesDiesel},
                merchandiseSales, restaurantSales, tobaccoSale, 0.00, 0.00);
    }

    private ShiftReport createShiftReport(ShiftReportDto shiftReportDto) {
        ShiftReport shiftReport = shiftReportRepository.findByShiftNumber(shiftReportDto.shiftNumber())
                .orElse(new ShiftReport());
        shiftReport.setDate(shiftReportDto.date());
        shiftReport.setShiftNumber(shiftReportDto.shiftNumber());
        shiftReport.setEmployeeID(shiftReportDto.employeeID());
        shiftReport.setManagerID(shiftReportDto.managerID());
        shiftReport.setPosCashTil1(shiftReportDto.posCashTil1());
        shiftReport.setPosCashTil2(shiftReportDto.posCashTil2());

//        FuelSales fuelSale = new FuelSales();
//        fuelSale.setRegularGasolineTransactions(shiftReportDto.fuelSales()[0]);
//        fuelSale.setMidGradeGasolineTransactions(shiftReportDto.fuelSales()[1]);
//        fuelSale.setPremiumGasolineTransactions(shiftReportDto.fuelSales()[2]);
//        fuelSale.setDieselTransactions(shiftReportDto.fuelSales()[3]);

        MerchandiseSales merchandiseSales = new MerchandiseSales();
        merchandiseSales.setMerchandiseSales(shiftReportDto.merchandiseSales());
        merchandiseSales.setNonRestaurantSales(shiftReportDto.nonRestaurantSales());
        merchandiseSales.setBottledBeverageSales(shiftReportDto.bottledBeverageSales());

        RestaurantSales restaurantSales = new RestaurantSales();
        restaurantSales.setTotalRestaurantSalesSales(shiftReportDto.restaurantSales());

        TobaccoSales tobaccoSales = new TobaccoSales();
        tobaccoSales.setTobaccoSales(shiftReportDto.tobaccoSales());

        //   fuelSale.setShiftReport(shiftReport);
        merchandiseSales.setShiftReport(shiftReport);
        restaurantSales.setShiftReport(shiftReport);
        tobaccoSales.setShiftReport(shiftReport);

        // Save the entities
        //  shiftReport.setFuelSales(fuelSale);
        shiftReport.setMerchandiseSales(merchandiseSales);
        shiftReport.setRestaurantSales(restaurantSales);
        shiftReport.setTobaccoSales(tobaccoSales);

        return shiftReport;
    }

    public List<List<InventoryDto>> parsePOSInventoryFile(MultipartFile inventoryReport) throws IOException {
        List<InventoryDto> bottledDrinkInventory = new ArrayList<>();
        List<InventoryDto> nonRestaurantInventory = new ArrayList<>();
        List<InventoryDto> restaurantInventory = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inventoryReport.getInputStream(), StandardCharsets.UTF_8));
            String line;
            boolean isBottled = false;
            boolean isNonRestaurant = false;
            boolean isRestaurant = false;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("BOTTLED_DRINKS_DETAILS")) {
                    isBottled = true;
                    isNonRestaurant = false;
                    isRestaurant = false;
                    continue;
                }

                if (line.startsWith("NON_RESTAURANT_DETAILS")) {
                    isBottled = false;
                    isNonRestaurant = true;
                    isRestaurant = false;
                    continue;
                }
                if (line.startsWith("RESTAURANT_DETAILS")) {
                    isBottled = false;
                    isNonRestaurant = false;
                    isRestaurant = true;
                    continue;
                }


                if (isBottled && line.startsWith("SKU_CODE")) {
                    String[] parts = line.split(",");
                    String skuCode = parts[0].split(":")[1].trim();
                    int qty = Integer.parseInt(parts[1].split(":")[1].trim());
                    bottledDrinkInventory.add(new InventoryDto("BOTTLED_DRINK", skuCode, qty));
                }

                if (isNonRestaurant && line.startsWith("SKU_CODE")) {
                    String[] parts = line.split(",");
                    String skuCode = parts[0].split(":")[1].trim();
                    int qty = Integer.parseInt(parts[1].split(":")[1].trim());
                    nonRestaurantInventory.add(new InventoryDto("NON_RESTAURANT", skuCode, qty));
                }
                if (isRestaurant && line.startsWith("SKU_CODE")) {
                    String[] parts = line.split(",");
                    String skuCode = parts[0].split(":")[1].trim();
                    int qty = Integer.parseInt(parts[1].split(":")[1].trim());
                    restaurantInventory.add(new InventoryDto("HOT_FOOD", skuCode, qty));
                }

            }
        } catch (Exception e) {
            //Change this!!!
            e.printStackTrace();
        }
        return List.of(bottledDrinkInventory, nonRestaurantInventory, restaurantInventory);
    }

    //  @Transactional
    public void parseShiftDataAndSaveToRepo(ShiftReportDto shiftReportDto) {
        ShiftReport s = createShiftReport(shiftReportDto);
        saveToRepository(s);
    }

    // @Transactional
    public void updateFuelInventory(ShiftReportDto shiftReportDto) throws IOException {
        merchandiseManagerClient.updateFuelInventoryReduceGallons(shiftReportDto.fuelSales());
    }

    private void saveToRepository(ShiftReport shiftReport) {
        shiftReportRepository.save(shiftReport);
    }

    //@Transactional
    public void updateInventory(List<List<InventoryDto>> inventoryList) {
        merchandiseManagerClient.updateMerchandiseInventoryFromSales(inventoryList);
    }

    public void pushToAccountingService(ShiftReport posReport) {
        //make API call to Revenue Management Controller
    }
}
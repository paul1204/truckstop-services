package com.truckstopservices.inventory.fuel.controller;

import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryDto;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleRequest;
import com.truckstopservices.inventory.fuel.dto.FuelSaleResponse;
import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import com.truckstopservices.inventory.fuel.service.FuelService;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryResponse;
import com.truckstopservices.inventory.fuel.exception.FuelSaleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("fuel")
public class FuelController {

    private final FuelService fuelService;

    public FuelController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping("/viewInventory")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<FuelInventoryResponse>> viewInventory() {
        List<FuelInventoryResponse> response = fuelService.getAllFuelInventory();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewInventoryChartData")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<FuelChartDataResponse>> viewInventoryChartData() {
        List<FuelChartDataResponse> chartData = fuelService.getFuelInventoryChartData();
        return ResponseEntity.ok(chartData);
    }

    @PostMapping("/update/FuelInventory/reduceGallons")
    public ResponseEntity<String> updateFuelInventoryReduceGallons(@RequestBody Double[] fuelSales) {
        fuelService.updateFuelInventoryDeductAvailableGallonsFromSales(fuelSales);
        return ResponseEntity.ok("Fuel Updated");
    }

    @PutMapping("/update/FuelInventory/FuelDelivery")
    public ResponseEntity<FuelDeliveryResponse> fuelDeliveryUpdateRepo(@RequestBody FuelDeliveryDto fuelDeliveryDto) throws Exception {
        return ResponseEntity.ok(fuelService.updateFuelDeliveryRepo(fuelDeliveryDto));
    }

    @PutMapping("/update/Diesel/FIFO")
    public ResponseEntity<FuelSaleResponse> updateDieselFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        FuelSaleResponse fuelSold = fuelService.updateDieselInventoryFIFOSales(fuelSaleRequest.gallonsSold(), fuelSaleRequest.terminal());
        return ResponseEntity.ok(fuelSold);
    }

    @PutMapping("/update/RegularFuel/FIFO")
    public ResponseEntity<FuelSaleResponse> updateRegularFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        FuelSaleResponse fuelSold = fuelService.updateRegularOctaneInventoryFIFOSales(fuelSaleRequest.gallonsSold(), fuelSaleRequest.terminal());
        return ResponseEntity.ok(fuelSold);
    }

    @PutMapping("/update/PremiumFuel/FIFO")
    public ResponseEntity<FuelSaleResponse> updatePremiumFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        FuelSaleResponse fuelSold = fuelService.updatePremiumOctaneInventoryFIFOSales(fuelSaleRequest.gallonsSold(), fuelSaleRequest.terminal());
        return ResponseEntity.ok(fuelSold);
    }
}

package com.truckstopservices.inventory.fuel.controller;

import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
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

    @Autowired
    private final FuelService fuelService;

    public FuelController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping("/viewInventory")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<FuelInventoryResponse>> viewInventory() {
        List<FuelInventoryResponse> response = fuelService.getAllFuelInventory();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/viewInventoryChartData")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<FuelChartDataResponse>> viewInventoryChartData() {
        List<FuelChartDataResponse> chartData = fuelService.getFuelInventoryChartData();
        return new ResponseEntity<>(chartData, HttpStatus.OK);
    }

    @PostMapping("/update/FuelInventory/reduceGallons")
    public ResponseEntity<String> updateFuelInventoryReduceGallons(@RequestBody Double[] fuelSales) {
        fuelService.updateFuelInventoryDeductAvailableGallonsFromSales(fuelSales);
        return new ResponseEntity<>("Fuel Updated", HttpStatus.OK);
    }

    @PutMapping("/update/FuelInventory/FuelDelivery")
    public ResponseEntity<FuelDeliveryResponse<FuelDelivery>> fuelDeliveryUpdateRepo(@RequestBody FuelDelivery fuelDelivery) {
        try {
            return new ResponseEntity<>(fuelService.updateFuelDeliveryRepo(fuelDelivery), HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DataAccessResourceFailureException("Failed to update fuel delivery: " + e.getMessage(), e);
        } catch (Exception e) {
            return new ResponseEntity<>(new FuelDeliveryResponse<>(false, e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/Diesel/FIFO")
    public ResponseEntity<FuelSaleResponse> updateDieselFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        try {
            FuelSaleResponse fuelSold = fuelService.updateDieselInventoryFIFOSales(fuelSaleRequest.gallonsSold());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            // Create a dummy receipt for the error case
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/RegularFuel/FIFO")
    public ResponseEntity<FuelSaleResponse> updateRegularFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        try {
            FuelSaleResponse fuelSold = fuelService.updateRegularOctaneInventoryFIFOSales(fuelSaleRequest.gallonsSold());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            // Create a dummy receipt for the error case
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/PremiumFuel/FIFO")
    public ResponseEntity<FuelSaleResponse> updatePremiumFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        try {
            FuelSaleResponse fuelSold = fuelService.updatePremiumOctaneInventoryFIFOSales(fuelSaleRequest.gallonsSold());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            // Create a dummy receipt for the error case
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

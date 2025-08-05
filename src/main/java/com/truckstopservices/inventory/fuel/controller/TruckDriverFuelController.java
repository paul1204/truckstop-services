package com.truckstopservices.inventory.fuel.controller;

import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleRequest;
import com.truckstopservices.inventory.fuel.dto.FuelSaleResponse;
import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import com.truckstopservices.inventory.fuel.service.TruckDriverFuelService;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryResponse;
import com.truckstopservices.inventory.fuel.exception.FuelSaleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("truckdriver/fuel")
public class TruckDriverFuelController {

    @Autowired
    private final TruckDriverFuelService truckDriverFuelService;

    public TruckDriverFuelController(TruckDriverFuelService truckDriverFuelService) {
        this.truckDriverFuelService = truckDriverFuelService;
    }

    @GetMapping("/viewInventory")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<FuelInventoryResponse>> viewInventory() {
        List<FuelInventoryResponse> response = truckDriverFuelService.getAllDieselInventory();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/viewInventoryChartData")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<FuelChartDataResponse>> viewInventoryChartData() {
        List<FuelChartDataResponse> chartData = truckDriverFuelService.getDieselInventoryChartData();
        return new ResponseEntity<>(chartData, HttpStatus.OK);
    }

    @PutMapping("/update/Diesel/FIFO")
    public ResponseEntity<FuelSaleResponse> updateDieselFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        try {
            FuelSaleResponse fuelSold = truckDriverFuelService.updateDieselInventoryFIFOSales(fuelSaleRequest.gallonsSold());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
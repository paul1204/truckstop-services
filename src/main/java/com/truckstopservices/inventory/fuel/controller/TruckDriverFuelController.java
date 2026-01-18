package com.truckstopservices.inventory.fuel.controller;

import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleHouseAccountResponse;
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
@RequestMapping("truck-driver/fuel")
public class TruckDriverFuelController {

    @Autowired
    private final TruckDriverFuelService truckDriverFuelService;

    public TruckDriverFuelController(TruckDriverFuelService truckDriverFuelService) {
        this.truckDriverFuelService = truckDriverFuelService;
    }

    @GetMapping("/viewInventoryChartData")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<FuelChartDataResponse>> viewInventoryChartData() {
        List<FuelChartDataResponse> chartData = truckDriverFuelService.getDieselInventoryChartData();
        return new ResponseEntity<>(chartData, HttpStatus.OK);
    }

    @PutMapping("/update/FuelInventory/FuelDelivery")
    public ResponseEntity<FuelDeliveryResponse<FuelDelivery>> fuelDeliveryUpdateRepo(@RequestBody FuelDelivery fuelDelivery) {
        try {
            return new ResponseEntity<>(truckDriverFuelService.updateDieselDeliveryRepo(fuelDelivery), HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DataAccessResourceFailureException("Failed to update fuel delivery: " + e.getMessage(), e);
        } catch (Exception e) {
            return new ResponseEntity<>(new FuelDeliveryResponse<>(false, e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/Diesel/FIFO")
    public ResponseEntity<FuelSaleResponse> updateDieselFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        try {
            FuelSaleResponse fuelSold = truckDriverFuelService.updateDieselInventoryFIFOSales(fuelSaleRequest.gallonsSold(), fuelSaleRequest.terminal());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            FuelSaleRequest errorRequest = new FuelSaleRequest(0, 0.0, 0.0, e.getMessage(), fuelSaleRequest.terminal());
            FuelSaleResponse errorResponse = FuelSaleResponse.fromFuelSaleRequestAndReceipt(errorRequest, null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/Diesel/FIFO/HouseAccount/{houseAccountId}")
    public ResponseEntity<FuelSaleHouseAccountResponse> updateDieselFuelFIFOHouseAccount(
            @RequestBody FuelSaleRequest fuelSaleRequest,
            @PathVariable String houseAccountId) {
        try {
            FuelSaleHouseAccountResponse fuelSold = truckDriverFuelService.updateDieselInventoryFIFOSalesHouseAccount(
                    fuelSaleRequest.gallonsSold(), 
                    houseAccountId,
                    fuelSaleRequest.terminal());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            FuelSaleRequest errorRequest = new FuelSaleRequest(0, 0.0, 0.0, e.getMessage(), fuelSaleRequest.terminal());
            FuelSaleHouseAccountResponse errorResponse = FuelSaleHouseAccountResponse.fromFuelSaleRequestAndHouseAccountTransaction(errorRequest, null, null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
package com.truckstopservices.inventory.fuel.controller;

import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleRequest;
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

import java.util.List;

@RestController
@RequestMapping("api/inventory/fuel")
public class FuelController {

    @Autowired
    private final FuelService fuelService;

    public FuelController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping("/viewInventory")
    public ResponseEntity<List<FuelInventoryResponse>> viewInventory() {
        List<FuelInventoryResponse> response = fuelService.getAllFuelInventory();
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<FuelSaleRequest> updateDieselFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        try {
            FuelSaleRequest fuelSold = fuelService.updateDieselInventoryFIFOSales(fuelSaleRequest.gallonsSold());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            return new ResponseEntity<>(new FuelSaleRequest(0, 0, 0, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/RegularFuel/FIFO")
    public ResponseEntity<FuelSaleRequest> updateRegularFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        try {
            FuelSaleRequest fuelSold = fuelService.updateRegularOctaneInventoryFIFOSales(fuelSaleRequest.gallonsSold());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            return new ResponseEntity<>(new FuelSaleRequest(0, 0, 0, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/PremiumFuel/FIFO")
    public ResponseEntity<FuelSaleRequest> updatePremiumFuelFIFO(@RequestBody FuelSaleRequest fuelSaleRequest) {
        try {
            FuelSaleRequest fuelSold = fuelService.updatePremiumOctaneInventoryFIFOSales(fuelSaleRequest.gallonsSold());
            return new ResponseEntity<>(fuelSold, HttpStatus.OK);
        } catch (FuelSaleException e) {
            return new ResponseEntity<>(new FuelSaleRequest(0, 0, 0, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
package com.truckstopservices.inventory.fuel.controller;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import com.truckstopservices.inventory.fuel.model.Fuel;
import com.truckstopservices.inventory.fuel.service.FuelService;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryResponse;
import com.truckstopservices.processing.dto.ShiftReportDto;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> updateFuelInventoryReduceGallons(@RequestBody Double[] fuelSales){
        fuelService.updateFuelInventoryDeductAvailableGallonsFromSales(fuelSales);
        return new ResponseEntity<>("Fuel Updated", HttpStatus.OK);
    }
    @PutMapping("/update/FuelInventory/FuelDelivery")
    public ResponseEntity<FuelDeliveryResponse<Fuel>> fuelDeliveryUpdateRepo(@RequestBody FuelDelivery fuelDelivery){
    try{
        return new ResponseEntity<>(fuelService.updateFuelDeliveryRepo(fuelDelivery), HttpStatus.OK);
    }
    catch (Exception e){
        //Throw better Exception.
        return new ResponseEntity<>(new FuelDeliveryResponse<>(false, e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
    }
    }
}

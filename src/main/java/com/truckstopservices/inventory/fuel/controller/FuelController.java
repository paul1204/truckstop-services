package com.truckstopservices.inventory.fuel.controller;


import com.truckstopservices.inventory.fuel.model.FuelModel;
import com.truckstopservices.inventory.fuel.service.FuelService;
import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.entity.ShiftReport;
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
    public ResponseEntity<List<?>> viewInventory() {
        List<?> response = fuelService.getAllFuelInventory();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/update/FuelInventory/reduceGallons")
    public ResponseEntity<List<?>> updateFuelInventoryReduceGallons(@RequestBody ShiftReportDto shiftReportDto){
        //shiftReport.getFuelSales().getRegularGasolineTransactions();
        fuelService.updateFuelInventoryDeductAvailableGallons(shiftReportDto);
        return ResponseEntity.ok().build();

    }

}

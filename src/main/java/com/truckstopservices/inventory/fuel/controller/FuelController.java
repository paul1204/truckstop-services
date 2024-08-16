package com.truckstopservices.inventory.fuel.controller;


import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import com.truckstopservices.inventory.fuel.model.FuelModel;
import com.truckstopservices.inventory.fuel.service.FuelService;
import com.truckstopservices.processing.dto.FuelDeliveryResponse;
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
        fuelService.updateFuelInventoryDeductAvailableGallons(shiftReportDto);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/update/FuelInventory/dieselDelivery")
    public ResponseEntity<FuelDeliveryResponse<Diesel>> dieselDeliveryUpdateInventory(@RequestParam double dieselDeliveryGallons){
    try{
        Diesel updatedDiesel = fuelService.recieveDieselFuelDelivery(dieselDeliveryGallons);
        return new ResponseEntity<>(new FuelDeliveryResponse<Diesel>(true, "Diesel Delivery Successful", updatedDiesel), HttpStatus.OK);
    }
    catch (Exception e){
        return new ResponseEntity<>(new FuelDeliveryResponse<Diesel>(false, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
    }
    @PutMapping("/update/FuelInventory/RegularOctaneDelivery")
    public ResponseEntity<FuelDeliveryResponse<RegularOctane>> recieveRegularOctaneFuelDelivery(@RequestParam double regularOctaneDeliveryGallons){
        try{
            RegularOctane updatedRegular = fuelService.recieveRegularOctaneFuelDelivery(regularOctaneDeliveryGallons);
            return new ResponseEntity<>(new FuelDeliveryResponse<RegularOctane>(true, "Regular Delivery Successful", updatedRegular), HttpStatus.OK);
        }
        //Throw Better Exception!
        catch(Exception e){
            return new ResponseEntity<>(new FuelDeliveryResponse<RegularOctane>(false, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update/FuelInventory/PremiumOctaneDelivery")
    public ResponseEntity<FuelDeliveryResponse<PremiumOctane>> recievePremiumOctaneFuelDelivery(@RequestParam double premiumOctaneDeliverGallons){
        try{
            PremiumOctane updatedPremium = fuelService.recievePremiumOctaneFuelDelivery(premiumOctaneDeliverGallons);
            return new ResponseEntity<>(new FuelDeliveryResponse<PremiumOctane>(true, "Premium Delivery Successful", updatedPremium), HttpStatus.OK);
        }
        //Throw Better Exception!
        catch(Exception e){
            return new ResponseEntity<>(new FuelDeliveryResponse<PremiumOctane>(false,e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

}

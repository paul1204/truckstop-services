package com.truckstopservices.inventory.manager.controller;

import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.inventory.merchandise.beverages.entity.BottledBeverage;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageCostByBrand;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageInventoryByBrand;
import com.truckstopservices.inventory.merchandise.model.Consumable;
import com.truckstopservices.inventory.merchandise.packagedfood.entity.PackagedFood;
import com.truckstopservices.inventory.merchandise.service.MerchandiseService;
import com.truckstopservices.inventory.restaurant.service.RestaurantService;
import com.truckstopservices.processing.dto.InventoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/inventory")
public class InventoryManagerController {

    @Autowired
    private final MerchandiseService merchandiseService;

    @Autowired
    private final RestaurantService restaurantService;


    public InventoryManagerController(MerchandiseService merchandiseService, RestaurantService restaurantService) {
        this.merchandiseService = merchandiseService;
        this.restaurantService = restaurantService;
    }

    @PutMapping
    public ResponseEntity<String> updateMerchandiseInventoryFromSales(@RequestBody List<InventoryDto> inventoryList){
        merchandiseService.reduceInventory(inventoryList);
        return new ResponseEntity<>("Inventory Updated", HttpStatus.OK);
    }

    @GetMapping("/bottledbeverageInventoryByBrandSqlAgg")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<BottledBeverageInventoryByBrand>> getBottledBeverageInventoryByBrandSqlAgg() {
        return new ResponseEntity<>(merchandiseService.getBottledBeverageInventoryByBrandSqlAgg(), HttpStatus.OK);
    }
    @GetMapping("/bottledbeverageInventoryCostByBrandSqlAgg")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<BottledBeverageCostByBrand>> returnInventoryCostByBrandSqlAgg() {
        return new ResponseEntity<>(merchandiseService.returnInventoryCostByBrandSqlAgg(), HttpStatus.OK);
    }

    @GetMapping("/bottledBeverages")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<BottledBeverage>> getAllBottledBeverages() {
        return new ResponseEntity<>(merchandiseService.getAllBottledBeverages(), HttpStatus.OK);
    }

    @GetMapping("/packagedFood")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<PackagedFood>> getAllPackagedFood() {
        return new ResponseEntity<>(merchandiseService.getAllPackagedFood(), HttpStatus.OK);
    }

    @GetMapping("/allMerchandise")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<Consumable>> getAllMerchandise() {
        return new ResponseEntity<>(merchandiseService.getAllMerchandise(), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/delivery/merchandise")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Invoice> orderInventoryReceiveMerchandiseDelivery(@RequestParam MultipartFile merchandiseInventoryOrder) throws IOException {
        Invoice invoice;
        try {
            invoice = merchandiseService.acceptMerchandiseDelivery(merchandiseInventoryOrder);
        }
        //Throw better Exception
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/delivery/restaurant")
    @ResponseStatus(HttpStatus.OK )
    public ResponseEntity<Invoice> orderInventoryReceiveRestaurantDelivery(@RequestParam MultipartFile merchandiseRestaurantOrder) throws IOException {
        Invoice invoice;
        try {
            invoice = restaurantService.acceptRestaurantDelivery(merchandiseRestaurantOrder);
        }
        //Throw better Exception
        catch (Exception e){
            return new ResponseEntity<>( null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

}

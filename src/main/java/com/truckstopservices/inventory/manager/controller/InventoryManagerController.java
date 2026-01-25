package com.truckstopservices.inventory.manager.controller;

import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.inventory.merchandise.beverages.entity.BottledBeverage;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageCostByBrand;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageInventoryByBrand;
import com.truckstopservices.inventory.merchandise.model.Consumable;
import com.truckstopservices.inventory.merchandise.packagedfood.entity.PackagedFood;
import com.truckstopservices.inventory.merchandise.dto.AllProductsChartData;
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

    private final MerchandiseService merchandiseService;

    private final RestaurantService restaurantService;

    public InventoryManagerController(MerchandiseService merchandiseService, RestaurantService restaurantService) {
        this.merchandiseService = merchandiseService;
        this.restaurantService = restaurantService;
    }

    @PutMapping
    public ResponseEntity<String> updateMerchandiseInventoryFromSales(@RequestBody List<InventoryDto> inventoryList){
        merchandiseService.reduceInventory(inventoryList);
        return ResponseEntity.ok("Inventory Updated");
    }

    @GetMapping("/bottledbeverageInventoryByBrandSqlAgg")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<BottledBeverageInventoryByBrand>> getBottledBeverageInventoryByBrandSqlAgg() {
        return ResponseEntity.ok(merchandiseService.getBottledBeverageInventoryByBrandSqlAgg());
    }
    @GetMapping("/bottledbeverageInventoryCostByBrandSqlAgg")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<BottledBeverageCostByBrand>> returnInventoryCostByBrandSqlAgg() {
        return ResponseEntity.ok(merchandiseService.returnInventoryCostByBrandSqlAgg());
    }

    @GetMapping("/bottledBeverages")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<BottledBeverage>> getAllBottledBeverages() {
        return ResponseEntity.ok(merchandiseService.getAllBottledBeverages());
    }

    @GetMapping("/packagedFood")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<PackagedFood>> getAllPackagedFood() {
        return ResponseEntity.ok(merchandiseService.getAllPackagedFood());
    }

    @GetMapping("/packagedFood/allProducts/chartData")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<AllProductsChartData> getAllPackagedFoodReturnChartData() {
        return ResponseEntity.ok(merchandiseService.getAllPackagedFoodReturnChartData());
    }

    @GetMapping("/bottledBeverage/allProducts/chartData")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<AllProductsChartData> getAllBottledBeverageReturnChartData() {
        return ResponseEntity.ok(merchandiseService.getAllBottledBeverageReturnChartData());
    }

    @GetMapping("/allMerchandise")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<List<Consumable>> getAllMerchandise() {
        return ResponseEntity.ok(merchandiseService.getAllMerchandise());
    }

    @PutMapping("/consumables/{skuCode}/max-capacity")
    @CrossOrigin(origins = "http://localhost:8000")
    public ResponseEntity<Double> updateConsumableMaxCapacity(
            @PathVariable String skuCode,
            @RequestParam Double maxCapacity) {
        Double ratio = merchandiseService.updateMaxCapacity(skuCode, maxCapacity);
        return ResponseEntity.ok(ratio);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/delivery/merchandise")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Invoice> orderInventoryReceiveMerchandiseDelivery(@RequestParam MultipartFile merchandiseInventoryOrder, @RequestParam String date) throws IOException {
        Invoice invoice = merchandiseService.acceptMerchandiseDelivery(merchandiseInventoryOrder, date);
        return ResponseEntity.ok(invoice);
    }
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/delivery/restaurant")
    @ResponseStatus(HttpStatus.OK )
    public ResponseEntity<Invoice> orderInventoryReceiveRestaurantDelivery(@RequestParam MultipartFile merchandiseRestaurantOrder) throws IOException {
        Invoice invoice = restaurantService.acceptRestaurantDelivery(merchandiseRestaurantOrder);
        return ResponseEntity.ok(invoice);
    }

}

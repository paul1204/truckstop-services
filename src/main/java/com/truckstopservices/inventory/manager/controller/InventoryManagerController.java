package com.truckstopservices.inventory.manager.controller;

import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.inventory.merchandise.service.MerchandiseService;
import com.truckstopservices.inventory.restaurant.service.RestaurantService;
import com.truckstopservices.processing.dto.InventoryDto;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/inventory/")
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
    public ResponseEntity<String> updateMerchandiseInventoryFromSales(@RequestBody List<List<InventoryDto>> inventoryList){
        merchandiseService.reduceInventory(inventoryList);
        return new ResponseEntity<>("Inventory Updated", HttpStatus.OK);
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

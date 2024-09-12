package com.truckstopservices.processing.client;


import com.truckstopservices.processing.dto.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "inventory-manager", url = "http://localhost:8080")
public interface MerchandiseManager {

    @RequestMapping(method = RequestMethod.PUT, value = "api/inventory")
    ResponseEntity<?> updateMerchandiseInventoryFromSales(List<List<InventoryDto>> inventoryList);
}

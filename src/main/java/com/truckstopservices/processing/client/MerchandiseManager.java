package com.truckstopservices.processing.client;
import com.truckstopservices.processing.dto.InventoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import java.util.List;

public interface MerchandiseManager {

    @PutExchange("api/inventory")
    ResponseEntity<String> updateMerchandiseInventoryFromSales(@RequestBody List<List<InventoryDto>> inventoryList);

    @PostExchange("api/inventory/fuel/update/FuelInventory/reduceGallons")
    ResponseEntity<String> updateFuelInventoryReduceGallons(@RequestBody Double[] fuelSales);
}

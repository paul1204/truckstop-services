package com.truckstopservices.inventory.restaurant.service;

import com.truckstopservices.accounting.invoice.service.implementation.InvoiceServiceImpl;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.inventory.merchandise.model.DeliveryItemInfo;
import com.truckstopservices.inventory.restaurant.entity.HotFood;
import com.truckstopservices.inventory.restaurant.repository.RestaurantRepository;
import com.truckstopservices.processing.dto.InventoryDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final InvoiceServiceImpl invoiceService;

    public RestaurantService(RestaurantRepository restaurantRepository, InvoiceServiceImpl invoiceService){
        this.restaurantRepository = restaurantRepository;
        this.invoiceService = invoiceService;
    }

    public void updateRestaurantInventory(InventoryDto product){
        updateInventory(product);
    }

    private void updateInventory(InventoryDto product){
        HotFood hotFood = restaurantRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Hot Food with " + product.skuCode() + " not found"));
        hotFood.reduceInventory(product.qty());
        restaurantRepository.save(hotFood);
    }

    @Transactional
    public Invoice acceptRestaurantDelivery(MultipartFile merchandiseRestaurantOrder) throws IOException {
        Map<String, DeliveryItemInfo> currentInventory = new HashMap<>();
        restaurantRepository.findAll().forEach((HotFood hotFood) -> {
            currentInventory.put(hotFood.getSkuCode(), new DeliveryItemInfo(hotFood.getQty(), "Hot Food"));
        });
        String rawDeliveryOrder = new String(merchandiseRestaurantOrder.getBytes(), StandardCharsets.UTF_8);
        String[] lines = rawDeliveryOrder.split("\n");

        //Totals
        double total = Double.parseDouble(lines[lines.length-1].split(",")[1]);
        String company = lines[0].split(",")[0];

        for(int i = 2; i < lines.length - 1; i++){
            String[] deliveryInfo = lines[i].split(",");
            //New Item, add to inventory
            if(!currentInventory.containsKey(deliveryInfo[0].trim())){
                restaurantRepository.save(new HotFood( deliveryInfo[0], deliveryInfo[1], Integer.parseInt(deliveryInfo[2]),
                        deliveryInfo[3], Double.parseDouble(deliveryInfo[4])
                ));
            }
            //Existing Product, update inventory
            else{
                HotFood hotFood = restaurantRepository.findBySkuCode(deliveryInfo[0]).orElseThrow(()-> new EntityNotFoundException("Not in Stock" + deliveryInfo[0] + " - " + deliveryInfo[1]));
                hotFood.increaseInventory(Integer.parseInt(deliveryInfo[2]));
            }
        }
        return invoiceService.createInvoice(company, "02/09/2025",total);
    }
}

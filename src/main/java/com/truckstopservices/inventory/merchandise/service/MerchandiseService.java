package com.truckstopservices.inventory.merchandise.service;

import com.truckstopservices.inventory.merchandise.beverages.entity.ColdBeverage;
import com.truckstopservices.inventory.merchandise.nonRestaurant.entity.NonRestaurantFood;
import com.truckstopservices.inventory.merchandise.repository.BeverageRepository;
import com.truckstopservices.inventory.merchandise.repository.NonRestaurantRepository;
//import com.truckstopservices.inventory.merchandise.dto.InventoryDto;
import com.truckstopservices.inventory.restaurant.entity.HotFood;
import com.truckstopservices.inventory.restaurant.repository.RestaurantRepository;
import com.truckstopservices.processing.dto.InventoryDto;
import com.truckstopservices.processing.entity.MerchandiseSales;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class MerchandiseService {

    @Autowired
    private BeverageRepository beverageRepository;

    @Autowired
    private NonRestaurantRepository nonRestaurantRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public MerchandiseService(BeverageRepository beverageRepository, NonRestaurantRepository nonRestaurantRepository, RestaurantRepository restaurantRepository) {
        this.beverageRepository = beverageRepository;
        this.nonRestaurantRepository = nonRestaurantRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void reduceInventory(List<List<InventoryDto>> inventoryList){
        List<InventoryDto> flattenItems = inventoryList.stream()
                .flatMap(List::stream)
                .toList();

        flattenItems.forEach(product -> {
            switch(product.inventoryType()){
                case "BOTTLED_DRINK" -> updateBeverageInventoryRepo(product);
                case "NON_RESTAURANT" -> updateNonRestaurantInventoryRepo(product);
                case "HOT_FOOD" -> updateRestaurantInventory(product);
            }
        });
    }

    private void updateBeverageInventoryRepo(InventoryDto product){
     ColdBeverage coldBeverage = beverageRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Product with " + product.skuCode() + " not found"));
     coldBeverage.reduceInventory(product.qty());
     beverageRepository.save(coldBeverage);
    }

    private void updateNonRestaurantInventoryRepo(InventoryDto product){
        NonRestaurantFood nonRestaurantFood = nonRestaurantRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Product with " + product.skuCode() + " not found"));
        nonRestaurantFood.reduceInventory(product.qty());
        nonRestaurantRepository.save(nonRestaurantFood);
    }
    private void updateRestaurantInventory(InventoryDto product){
        HotFood hotFood = restaurantRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Product with " + product.skuCode() + " not found"));
        hotFood.reduceInventory(product.qty());
        restaurantRepository.save(hotFood);
    }
}

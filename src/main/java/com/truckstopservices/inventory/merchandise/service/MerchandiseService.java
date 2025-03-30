package com.truckstopservices.inventory.merchandise.service;

import com.truckstopservices.accounting.accountsPayable.service.implementation.AccountsPayableImplementation;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.inventory.merchandise.beverages.entity.ColdBeverage;
import com.truckstopservices.inventory.merchandise.model.Consumable;
import com.truckstopservices.inventory.merchandise.model.DeliveryItemInfo;
//import com.truckstopservices.inventory.merchandise.model.DeliveryItemType;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MerchandiseService {

    @Autowired
    private BeverageRepository beverageRepository;

    @Autowired
    private NonRestaurantRepository nonRestaurantRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AccountsPayableImplementation accountsPayableImplementation;


    public MerchandiseService(BeverageRepository beverageRepository, NonRestaurantRepository nonRestaurantRepository,
                              RestaurantRepository restaurantRepository, AccountsPayableImplementation accountsPayableImplementation) {
        this.beverageRepository = beverageRepository;
        this.nonRestaurantRepository = nonRestaurantRepository;
        this.restaurantRepository = restaurantRepository;
        this.accountsPayableImplementation = accountsPayableImplementation;
    }

    @Transactional
    public Invoice acceptMerchandiseDelivery(MultipartFile merchandiseInventoryOrder) throws IOException {
        Map<String, DeliveryItemInfo> currentInventory = new HashMap<>();
        int beverageCount = (int) beverageRepository.count();
        int nonRestaurantRepositoryCount = (int) nonRestaurantRepository.count();
        Invoice invoice;

        //Get Current Inventory
        beverageRepository.findAll().forEach((ColdBeverage bottle)->{
            currentInventory.put(bottle.getSkuCode(), new DeliveryItemInfo(bottle.getQty(), "Beverage"));
        });

        nonRestaurantRepository.findAll().forEach((NonRestaurantFood nonRestaurantFood)->{
            currentInventory.put(nonRestaurantFood.getSkuCode(), new DeliveryItemInfo(nonRestaurantFood.getQty(), "Non Restaurant"));
        });

        //Parse Merchandise Order
        String rawDeliveryOrder = new String(merchandiseInventoryOrder.getBytes(), StandardCharsets.UTF_8);
        String[] lines = rawDeliveryOrder.split("\n");



        //Totals
        double total = Double.parseDouble(lines[lines.length - 1].split(",")[1]);
        String company = lines[0].split(",")[0];

        for(int i = 2; i <= lines.length-1; i++){
            String[] merchandiseInfo = lines[i].split(",");

            //New Item, add to inventory
            if(!currentInventory.containsKey(merchandiseInfo[0].trim())) {
                // System.out.println(merchandiseInfo[0].trim());
                if (Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "D")) {
                    beverageRepository.save(new ColdBeverage(
                            merchandiseInfo[0], merchandiseInfo[1], Double.parseDouble(merchandiseInfo[2]), merchandiseInfo[3],
                            Integer.parseInt(merchandiseInfo[4]), merchandiseInfo[5]
                    ));
                }
                if (Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "NR")) {
                    nonRestaurantRepository.save(new NonRestaurantFood(
                            merchandiseInfo[0], merchandiseInfo[1], Double.parseDouble(merchandiseInfo[2]), merchandiseInfo[3],
                            Integer.parseInt(merchandiseInfo[4]), merchandiseInfo[5]
                    ));
                }
            }
            //Existing Product, update inventory
            else{
                if(Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "D")){
                        ColdBeverage restockBeverage = beverageRepository.findBySkuCode(merchandiseInfo[0]).orElseThrow(()-> new EntityNotFoundException("Not in Stock" + merchandiseInfo[0] + " - " + merchandiseInfo[1] ));
                        restockBeverage.increaseInventory(Integer.parseInt(merchandiseInfo[4]));
                }
                if(Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "NR")){
                    NonRestaurantFood restockFood = nonRestaurantRepository.findBySkuCode(merchandiseInfo[0]).orElseThrow(()-> new EntityNotFoundException("Not in Stock" + merchandiseInfo[0] + " - " + merchandiseInfo[1] ));
                    restockFood.increaseInventory(Integer.parseInt(merchandiseInfo[4]));
                }
            }
        }

        return accountsPayableImplementation.createInvoice(company, "02/09/2025",total);
    }

    private void createInvoiceForMerchant(String company,Double total){
        //Create Invoice, Make payable to company
        //accountsPayableImplementation.createInvoice(company, "02/09/2025",total);
        //Process Payment
    }

    public void reduceInventory(List<List<InventoryDto>> inventoryList){
        List<InventoryDto> flattenItems = inventoryList.stream().flatMap(List::stream).toList();
        flattenItems.forEach(product -> {
            switch(product.inventoryType()){
                case "BOTTLED_DRINK" -> updateBeverageInventoryRepo(product);
                case "NON_RESTAURANT" -> updateNonRestaurantInventoryRepo(product);
                case "HOT_FOOD" -> updateRestaurantInventory(product);
            }
        });
    }

    private void updateBeverageInventoryRepo(InventoryDto product){
     ColdBeverage coldBeverage = beverageRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Bottled Drink with " + product.skuCode() + " not found"));
     coldBeverage.reduceInventory(product.qty());
     beverageRepository.save(coldBeverage);
    }

    private void updateNonRestaurantInventoryRepo(InventoryDto product){
        NonRestaurantFood nonRestaurantFood = nonRestaurantRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Non Restaurant Food with " + product.skuCode() + " not found"));
        nonRestaurantFood.reduceInventory(product.qty());
        nonRestaurantRepository.save(nonRestaurantFood);
    }
    private void updateRestaurantInventory(InventoryDto product){
        HotFood hotFood = restaurantRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Hot Food with " + product.skuCode() + " not found"));
        hotFood.reduceInventory(product.qty());
        restaurantRepository.save(hotFood);
    }
}

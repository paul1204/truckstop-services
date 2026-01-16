package com.truckstopservices.inventory.merchandise.service;

import com.truckstopservices.accounting.invoice.service.implementation.InvoiceServiceImpl;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.inventory.merchandise.beverages.entity.BottledBeverage;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageCostByBrand;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageInventoryByBrand;
import com.truckstopservices.inventory.merchandise.model.DeliveryItemInfo;
//import com.truckstopservices.inventory.merchandise.model.DeliveryItemType;
import com.truckstopservices.inventory.merchandise.packagedfood.entity.PackagedFood;
import com.truckstopservices.inventory.merchandise.repository.BottledBeverageRepository;
import com.truckstopservices.inventory.merchandise.repository.PackagedFoodRepository;
//import com.truckstopservices.inventory.merchandise.dto.InventoryDto;
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
import java.util.*;


@Service
public class MerchandiseService {

    @Autowired
    private BottledBeverageRepository bottledBeverageRepository;

    @Autowired
    private PackagedFoodRepository packagedFoodRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private InvoiceServiceImpl invoiceService;


    public MerchandiseService(BottledBeverageRepository bottledBeverageRepository, PackagedFoodRepository packagedFoodRepository,
                              RestaurantRepository restaurantRepository, InvoiceServiceImpl invoiceService) {
        this.bottledBeverageRepository = bottledBeverageRepository;
        this.packagedFoodRepository = packagedFoodRepository;
        this.restaurantRepository = restaurantRepository;
        this.invoiceService = invoiceService;
    }

    public List<BottledBeverageInventoryByBrand> getAllBottledBeverages() {
        return bottledBeverageRepository.findInventoryByBrand();
    }

    public List<BottledBeverageCostByBrand> returnInventoryCostByBrand() {
        return bottledBeverageRepository.returnInventoryCostByBrand();
    }

    @Transactional
    public Invoice acceptMerchandiseDelivery(MultipartFile merchandiseInventoryOrder) throws IOException {
        Map<String, DeliveryItemInfo> currentInventory = new HashMap<>();
        int bottledBeverageCount = (int) bottledBeverageRepository.count();
        int packagedFoodRepositoryCount = (int) packagedFoodRepository.count();
        Invoice invoice;

        //Get Current Inventory
        bottledBeverageRepository.findAll().forEach((BottledBeverage bottle)->{
            currentInventory.put(bottle.getSkuCode(), new DeliveryItemInfo(bottle.getQty(), "BOTTLED_BEVERAGE"));
        });

        packagedFoodRepository.findAll().forEach((PackagedFood packagedFood)->{
            currentInventory.put(packagedFood.getSkuCode(), new DeliveryItemInfo(packagedFood.getQty(), "PACKAGED_FOOD"));
        });

        //Parse Merchandise Order
        String rawDeliveryOrder = new String(merchandiseInventoryOrder.getBytes(), StandardCharsets.UTF_8);
        String[] lines = rawDeliveryOrder.split("\n");



        //Totals
        double total = Double.parseDouble(lines[lines.length - 1].split(",")[1]);
        String company = lines[0].split(",")[0];

        for(int i = 1; i <= lines.length-1; i++){
            String[] merchandiseInfo = lines[i].split(",");

            //New Item, add to inventory
            if(!currentInventory.containsKey(merchandiseInfo[0].trim())) {
                // System.out.println(merchandiseInfo[0].trim());
                if (Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "D")) {
                    bottledBeverageRepository.save(new BottledBeverage(
                            merchandiseInfo[0], merchandiseInfo[1], Double.parseDouble(merchandiseInfo[2]), merchandiseInfo[3],
                            Integer.parseInt(merchandiseInfo[4]), merchandiseInfo[5]
                    ));
                }
                if (Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "NR")) {
                    packagedFoodRepository.save(new PackagedFood(
                            merchandiseInfo[0], merchandiseInfo[1], Double.parseDouble(merchandiseInfo[2]), merchandiseInfo[3],
                            Integer.parseInt(merchandiseInfo[4]), merchandiseInfo[5]
                    ));
                }
            }
            //Existing Product, update inventory
            else{
                if(Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "D")){
                        BottledBeverage restockBeverage = bottledBeverageRepository.findBySkuCode(merchandiseInfo[0]).orElseThrow(()-> new EntityNotFoundException("Not in Stock" + merchandiseInfo[0] + " - " + merchandiseInfo[1] ));
                        restockBeverage.increaseInventory(Integer.parseInt(merchandiseInfo[4]));
                }
                if(Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "NR")){
                    PackagedFood restockFood = packagedFoodRepository.findBySkuCode(merchandiseInfo[0]).orElseThrow(()-> new EntityNotFoundException("Not in Stock" + merchandiseInfo[0] + " - " + merchandiseInfo[1] ));
                    restockFood.increaseInventory(Integer.parseInt(merchandiseInfo[4]));
                }
            }
        }

        return invoiceService.createInvoice(company, "02/09/2025",total);
    }

    private void createInvoiceForMerchant(String company,Double total){
        //Create Invoice, Make payable to company
        //invoiceService.createInvoice(company, "02/09/2025",total);
        //Process Payment
    }

    public void reduceInventory(List<InventoryDto> inventoryList){
        List<InventoryDto> flattenItems = inventoryList.stream().toList();
        inventoryList.forEach(product -> {
            switch(product.inventoryType()){
                case "BOTTLED_BEVERAGE" -> updateBottledBeverageInventoryRepo(product);
                case "NON_RESTAURANT" -> updatePackagedFoodInventoryRepo(product);
                case "HOT_FOOD" -> updateRestaurantInventory(product);
            }
        });
    }

    private void updateBottledBeverageInventoryRepo(InventoryDto product){
     BottledBeverage bottledBeverage = bottledBeverageRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Bottled Beverage with " + product.skuCode() + " not found"));
     bottledBeverage.reduceInventory(product.qty());
     bottledBeverageRepository.save(bottledBeverage);
    }

    private void updatePackagedFoodInventoryRepo(InventoryDto product){
        PackagedFood packagedFood = packagedFoodRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Packaged Food with " + product.skuCode() + " not found"));
        packagedFood.reduceInventory(product.qty());
        packagedFoodRepository.save(packagedFood);
    }
    private void updateRestaurantInventory(InventoryDto product){
        HotFood hotFood = restaurantRepository.findBySkuCode(product.skuCode()).orElseThrow(()-> new EntityNotFoundException("Hot Food with " + product.skuCode() + " not found"));
        hotFood.reduceInventory(product.qty());
        restaurantRepository.save(hotFood);
    }
}

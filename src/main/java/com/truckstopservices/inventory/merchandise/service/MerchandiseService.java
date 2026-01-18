package com.truckstopservices.inventory.merchandise.service;

import com.truckstopservices.accounting.invoice.service.implementation.InvoiceServiceImpl;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.accounting.sales.entity.SalesItem;
import com.truckstopservices.accounting.sales.receipt.Receipt;
import com.truckstopservices.accounting.sales.service.SalesService;
import com.truckstopservices.common.types.SalesType;
import com.truckstopservices.inventory.merchandise.beverages.entity.BottledBeverage;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageCostByBrand;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageInventoryByBrand;
import com.truckstopservices.inventory.merchandise.model.DeliveryItemDto;
import com.truckstopservices.inventory.merchandise.model.DeliveryItemInfo;
//import com.truckstopservices.inventory.merchandise.model.DeliveryItemType;
import com.truckstopservices.inventory.merchandise.model.Consumable;
import com.truckstopservices.inventory.merchandise.packagedfood.entity.PackagedFood;
import com.truckstopservices.inventory.merchandise.repository.BottledBeverageRepository;
import com.truckstopservices.inventory.merchandise.repository.PackagedFoodRepository;
//import com.truckstopservices.inventory.merchandise.dto.InventoryDto;
import com.truckstopservices.inventory.restaurant.entity.HotFood;
import com.truckstopservices.inventory.restaurant.repository.RestaurantRepository;
import com.truckstopservices.posdataingest.model.POSSaleDto;
import com.truckstopservices.processing.dto.InventoryDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


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

    @Autowired
    private SalesService salesService;

    public MerchandiseService(BottledBeverageRepository bottledBeverageRepository, PackagedFoodRepository packagedFoodRepository,
                              RestaurantRepository restaurantRepository, InvoiceServiceImpl invoiceService, SalesService salesService) {
        this.bottledBeverageRepository = bottledBeverageRepository;
        this.packagedFoodRepository = packagedFoodRepository;
        this.restaurantRepository = restaurantRepository;
        this.invoiceService = invoiceService;
        this.salesService = salesService;
    }

    public List<BottledBeverageInventoryByBrand> getBottledBeverageInventoryByBrandSqlAgg() {
        return bottledBeverageRepository.findInventoryByBrandSqlAgg();
    }

    public List<BottledBeverage> getAllBottledBeverages() {
        return bottledBeverageRepository.findAll();
    }

    public List<PackagedFood> getAllPackagedFood() {
        return packagedFoodRepository.findAll();
    }

    public List<BottledBeverageCostByBrand> returnInventoryCostByBrandSqlAgg() {
        return bottledBeverageRepository.returnInventoryCostByBrandSqlAgg();
    }

    public List<Consumable> getAllMerchandise() {
        List<Consumable> allMerchandise = new ArrayList<>();
        allMerchandise.addAll(bottledBeverageRepository.findAll());
        allMerchandise.addAll(packagedFoodRepository.findAll());
        return allMerchandise;
    }

    @Transactional
    public Invoice acceptMerchandiseDelivery(MultipartFile merchandiseInventoryOrder) throws IOException {
        Map<String, DeliveryItemDto> currentInventory = new HashMap<>();
        int bottledBeverageCount = (int) bottledBeverageRepository.count();
        int packagedFoodRepositoryCount = (int) packagedFoodRepository.count();
        Invoice invoice;

        //Get Current Inventory
        bottledBeverageRepository.findAll().forEach((BottledBeverage bottle)->{
            currentInventory.put(bottle.getSkuCode(), new DeliveryItemDto(bottle.getQty(), "BOTTLED_BEVERAGE"));
        });

        packagedFoodRepository.findAll().forEach((PackagedFood packagedFood)->{
            currentInventory.put(packagedFood.getSkuCode(), new DeliveryItemDto(packagedFood.getQty(), "PACKAGED_FOOD"));
        });

        //Parse Merchandise Order
        String rawDeliveryOrder = new String(merchandiseInventoryOrder.getBytes(), StandardCharsets.UTF_8);
        String[] lines = rawDeliveryOrder.split("\n");
        
        
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        
        //Totals
        double total = Double.parseDouble(lines[lines.length - 1].split(",")[1]);
        String company = lines[0].split(",")[0];

        for(int i = 1; i <= lines.length-1; i++){
            String[] merchandiseInfo = lines[i].split(",");

            //New Item, add to inventory
            if(!currentInventory.containsKey(merchandiseInfo[0].trim())) {
                // System.out.println(merchandiseInfo[0].trim());
                if (Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "D")) {
                    BottledBeverage newBeverage = new BottledBeverage(
                            merchandiseInfo[0], merchandiseInfo[1], Double.parseDouble(merchandiseInfo[2]), merchandiseInfo[3],
                            Integer.parseInt(merchandiseInfo[4]), merchandiseInfo[5]
                    );
                    newBeverage.addDelivery(today);
                    bottledBeverageRepository.save(newBeverage);
                }
                if (Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "NR")) {
                    PackagedFood newFood = new PackagedFood(
                            merchandiseInfo[0], merchandiseInfo[1], Double.parseDouble(merchandiseInfo[2]), merchandiseInfo[3],
                            Integer.parseInt(merchandiseInfo[4]), merchandiseInfo[5]
                    );
                    newFood.addDelivery(today);
                    packagedFoodRepository.save(newFood);
                }
            }
            //Existing Product, update inventory
            else{
                if(Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "D")){
                        BottledBeverage restockBeverage = bottledBeverageRepository.findBySkuCode(merchandiseInfo[0]).orElseThrow(()-> new EntityNotFoundException("Not in Stock" + merchandiseInfo[0] + " - " + merchandiseInfo[1] ));
                        restockBeverage.increaseInventory(Integer.parseInt(merchandiseInfo[4]));
                        restockBeverage.addDelivery(today);
                }
                if(Objects.equals(merchandiseInfo[merchandiseInfo.length - 1].trim(), "NR")){
                            PackagedFood restockFood = packagedFoodRepository.findBySkuCode(merchandiseInfo[0]).orElseThrow(()-> new EntityNotFoundException("Not in Stock" + merchandiseInfo[0] + " - " + merchandiseInfo[1] ));
                            restockFood.increaseInventory(Integer.parseInt(merchandiseInfo[4]));
                            restockFood.addDelivery(today);
                    }
            }
        }

        return invoiceService.createInvoice(company, today, total);
    }

    private void createInvoiceForMerchant(String company,Double total){
        //Create Invoice, Make payable to company
        //invoiceService.createInvoice(company, "02/09/2025",total);
        //Process Payment
    }

    public Receipt reduceInventoryV2(POSSaleDto posSaleDto){
        posSaleDto.salesItems().forEach(product -> {
            switch(product.getSalesType()){
                case SalesType.BOTTLED_BEVERAGE -> updateBottledBeverageInventoryRepo(product);
                case SalesType.PACKAGED_FOOD -> updatePackagedFoodInventoryRepo(product);
              //  case SalesType.RESTAURANT -> updateRestaurantInventory(product);
            }
        });
        return salesService.createSaleFromPOS(posSaleDto);
    }

    public void reduceInventory(List<InventoryDto> inventoryList){
        List<InventoryDto> flattenItems = inventoryList.stream().toList();
        inventoryList.forEach(product -> {
            switch(product.salesType()){
           //     case SalesType.BOTTLED_BEVERAGE -> updateBottledBeverageInventoryRepo(product);
           //     case SalesType.PACKAGED_FOOD -> updatePackagedFoodInventoryRepo(product);
           //     case SalesType.RESTAURANT -> updateRestaurantInventory(product);
            }
        });
    }

    private void updateBottledBeverageInventoryRepo(SalesItem product){
     BottledBeverage bottledBeverage = bottledBeverageRepository.findBySkuCode(product.getSkuCode()).orElseThrow(()-> new EntityNotFoundException("Bottled Beverage with " + product.getSkuCode() + " not found"));
     bottledBeverage.reduceInventory(product.getQuantity());
     bottledBeverageRepository.save(bottledBeverage);
    }

    private void updatePackagedFoodInventoryRepo(SalesItem product){
        PackagedFood packagedFood = packagedFoodRepository.findBySkuCode(product.getSkuCode()).orElseThrow(()-> new EntityNotFoundException("Packaged Food with " + product.getSkuCode() + " not found"));
        packagedFood.reduceInventory(product.getQuantity());
        packagedFoodRepository.save(packagedFood);
    }
    private void updateRestaurantInventory(SalesItem product){
        HotFood hotFood = restaurantRepository.findBySkuCode(product.getSkuCode()).orElseThrow(()-> new EntityNotFoundException("Hot Food with " + product.getSkuCode() + " not found"));
      //  hotFood.reduceInventory(product.getQuantity());
        restaurantRepository.save(hotFood);
    }
}

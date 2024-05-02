package com.truckstopservices.inventory.manager.service;

import com.truckstopservices.inventory.manager.entity.StockBalance;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryOrderService {

    //
    //StockBalance stock = new StockBalance();
    private StockBalance stockBalance;

    @Autowired
    public InventoryOrderService(StockBalance stockBalance){
        this.stockBalance = stockBalance;
    }
}

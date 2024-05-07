package com.truckstopservices.inventory.manager.service;
//package com.truckstopservices.inventory.merchandise.beverages;

import com.truckstopservices.inventory.manager.entity.InitialInventory;
import com.truckstopservices.inventory.manager.entity.OrderDTO;
import com.truckstopservices.inventory.merchandise.beverages.entity.Beverage;
import com.truckstopservices.inventory.merchandise.snacks.model.Snack;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InventoryBalanceService {

      private InitialInventory initInventory;
      @Autowired
      public InventoryBalanceService(InitialInventory initInventory){
          this.initInventory = initInventory;
    }


    public void updateInventory(OrderDTO order) {
          //consume the order
        //create repo, update DB
    }
}

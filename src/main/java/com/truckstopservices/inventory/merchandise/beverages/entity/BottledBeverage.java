package com.truckstopservices.inventory.merchandise.beverages.entity;

import com.truckstopservices.inventory.merchandise.model.Consumable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "bottled_beverage")
public class BottledBeverage extends Consumable {

    public BottledBeverage() {
        super();
    }

    public BottledBeverage(String skuCode, String name, double costOfGoods, String brand, int qty, String size) {
        super(skuCode, name, costOfGoods, brand, qty, size);
    }

}

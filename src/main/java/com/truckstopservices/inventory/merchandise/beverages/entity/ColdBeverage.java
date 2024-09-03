package com.truckstopservices.inventory.merchandise.beverages.entity;

import com.truckstopservices.inventory.merchandise.config.Brands;
import com.truckstopservices.inventory.merchandise.model.Consumable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cold_beverage")
public class ColdBeverage extends Consumable {

    public ColdBeverage() {
        super();
    }

    public ColdBeverage(String skuCode, String name, double price, String brand, int qty, String size) {
        super(skuCode, name, price, brand, qty, size);
    }

}

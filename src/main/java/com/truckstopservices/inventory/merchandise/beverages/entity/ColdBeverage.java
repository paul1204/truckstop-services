package com.truckstopservices.inventory.merchandise.beverages.entity;

import com.truckstopservices.inventory.merchandise.config.Brands;
import com.truckstopservices.inventory.merchandise.model.Consumable;
import jakarta.persistence.Entity;

@Entity
public class ColdBeverage extends Consumable {

    public ColdBeverage(String skuCode, String name, double price, String brand, int qty) {
        super(skuCode, name, price, brand, qty);
    }
}

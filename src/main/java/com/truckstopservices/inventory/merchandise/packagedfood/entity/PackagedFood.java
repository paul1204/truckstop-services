package com.truckstopservices.inventory.merchandise.packagedfood.entity;

import com.truckstopservices.inventory.merchandise.model.Consumable;
import jakarta.persistence.Entity;

@Entity
public class PackagedFood extends Consumable {

    public PackagedFood(){super();}
    public PackagedFood(String skuCode, String name, double costOfGoods, String brand, int qty, String size) {
        super(skuCode, name, costOfGoods, brand, qty, size);
    }
}

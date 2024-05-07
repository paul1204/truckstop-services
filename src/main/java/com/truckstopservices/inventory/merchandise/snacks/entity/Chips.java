package com.truckstopservices.inventory.merchandise.snacks.entity;

import com.truckstopservices.inventory.merchandise.config.Brands;
import com.truckstopservices.inventory.merchandise.snacks.model.Snack;

public class Chips<T> extends Snack {

    public Chips(String skuCode, String name, double price, Brands brand, int qty) {
        super(skuCode, name, price, brand, qty);
    }

}

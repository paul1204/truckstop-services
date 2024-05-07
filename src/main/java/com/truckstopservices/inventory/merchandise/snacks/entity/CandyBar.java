package com.truckstopservices.inventory.merchandise.snacks.entity;

import com.truckstopservices.inventory.merchandise.config.Brands;
import com.truckstopservices.inventory.merchandise.snacks.model.Snack;

public class CandyBar<T> extends Snack {

    public CandyBar(String skuCode, String name, double price, Brands brand) {
        super(skuCode, name, price, brand);
    }
}

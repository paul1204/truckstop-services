package com.truckstopservices.inventory.restaurant.entity;

import com.truckstopservices.inventory.restaurant.model.RestaurantModel;
import jakarta.persistence.Entity;

@Entity
public class HotFood extends RestaurantModel {
    public HotFood(){}

    public HotFood(String skuCode, String name, int qty, String size, double price) {
        super(skuCode, name, qty, size, price);
    }

}
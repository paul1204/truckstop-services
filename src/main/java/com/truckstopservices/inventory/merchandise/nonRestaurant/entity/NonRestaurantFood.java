package com.truckstopservices.inventory.merchandise.nonRestaurant.entity;

import com.truckstopservices.inventory.merchandise.model.Consumable;
import jakarta.persistence.Entity;

@Entity
public class NonRestaurantFood extends Consumable {
    public NonRestaurantFood(String skuCode, String name, double price, String brand, int qty, String size) {
        super(skuCode, name, price, brand, qty, size);
    }
}

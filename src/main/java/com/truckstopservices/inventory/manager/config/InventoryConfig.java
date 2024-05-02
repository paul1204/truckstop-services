package com.truckstopservices.inventory.manager.config;

import com.truckstopservices.inventory.manager.entity.StockBalance;
import com.truckstopservices.inventory.merchandise.Brands;
import com.truckstopservices.inventory.merchandise.beverages.entity.Beverage;
import com.truckstopservices.inventory.merchandise.snacks.entity.Chips;
import com.truckstopservices.inventory.merchandise.snacks.model.Snack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class InventoryConfig {

    @Bean
    public StockBalance initalInventory(){
        List<Snack> initalSnacks = Arrays.asList(
                new Chips("123", "Plain", 1.49, Brands.LAYS),
                new Chips("456", "Spicy", 1.49, Brands.LAYS),
                new Chips("789", "Cool Ranch", 1.49, Brands.DORITOS)
                // Add more predefined snacks as needed
        );
        List<Beverage> initalDrinks = Arrays.asList(
        new Beverage("B123", "Coca Cola", "12oz", 1.49, Brands.COCACOLA),
        new Beverage("B456", "Diet Coke", "12oz", 1.49, Brands.COCACOLA),
        new Beverage("B789", "Sprite", "12oz", 1.49, Brands.COCACOLA),
        new Beverage("B123", "Sprite Zero", "12oz", 1.49, Brands.COCACOLA)
                // Add more predefined snacks as needed
        );
        return new StockBalance(initalSnacks, initalDrinks);
    }
}

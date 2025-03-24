//package com.truckstopservices.inventory.manager.config;
//
//import com.truckstopservices.inventory.manager.entity.InitialInventory;
//import com.truckstopservices.inventory.merchandise.config.Brands;
//import com.truckstopservices.inventory.merchandise.beverages.entity.Beverage;
//import com.truckstopservices.inventory.merchandise.snacks.entity.Chips;
//import com.truckstopservices.inventory.merchandise.snacks.model.Snack;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class InventoryConfig {
//
//    @Bean
//    public InitialInventory initalInventory(){
//        List<Snack> initalSnacks = Arrays.asList(
//                new Chips("123", "Plain", 1.49, Brands.LAYS,5),
//                new Chips("456", "Spicy", 1.49, Brands.LAYS,5),
//                new Chips("789", "Cool Ranch", 1.49, Brands.DORITOS,5)
//                // Add more predefined snacks as needed
//        );
//        List<Beverage> initalDrinks = Arrays.asList(
//        new Beverage("B123", "Coca Cola", "12oz", 1.49, Brands.COCACOLA,5),
//        new Beverage("B456", "Diet Coke", "12oz", 1.49, Brands.COCACOLA,5),
//        new Beverage("B789", "Sprite", "12oz", 1.49, Brands.COCACOLA,5),
//        new Beverage("B123", "Sprite Zero", "12oz", 1.49, Brands.COCACOLA,5),
//        new Beverage("B963", "Vanilla Coke", "12oz", 1.59, Brands.COCACOLA,5)
//                // Add more predefined snacks as needed
//        );
//        return new InitialInventory(initalSnacks, initalDrinks);
//    }
//}

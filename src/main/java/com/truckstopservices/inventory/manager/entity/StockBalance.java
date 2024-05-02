package com.truckstopservices.inventory.manager.entity;
//package com.truckstopservices.inventory.merchandise.beverages;

import com.truckstopservices.inventory.merchandise.Brands;
import com.truckstopservices.inventory.merchandise.beverages.entity.Beverage;
import com.truckstopservices.inventory.merchandise.snacks.entity.CandyBar;
import com.truckstopservices.inventory.merchandise.snacks.entity.Chips;
import com.truckstopservices.inventory.merchandise.snacks.model.Snack;

import java.util.ArrayList;
import java.util.List;

public class StockBalance {

    private List<Snack> snacks;
    private List<Beverage> drinks;

    public StockBalance(List<Snack> snacks, List<Beverage> drinks){
        this.snacks = snacks;
        this.drinks = drinks;
//        initalizeSnacks();
//        initalizeDrinks();
    }

//    private void initalizeSnacks(){
//        snacks.add(new Chips("C123","Plain", 1.49, Brands.LAYS));
//        snacks.add(new Chips("C456","Spicy", 1.49, Brands.LAYS));
//        snacks.add(new Chips("C789","Cool Ranch", 1.49, Brands.DORITOS));
//        snacks.add(new CandyBar("C963","Reese's Cup", 0.99, Brands.LAYS));
//        snacks.add(new CandyBar("C852","Kit Kat", 0.99, Brands.LAYS));
//    }
//
//    private void initalizeDrinks(){
//        drinks.add(new Beverage("B123", "Coca Cola", "12oz", 1.49, Brands.COCACOLA));
//        drinks.add(new Beverage("B456", "Diet Coke", "12oz", 1.49, Brands.COCACOLA));
//        drinks.add(new Beverage("B789", "Sprite", "12oz", 1.49, Brands.COCACOLA));
//        drinks.add(new Beverage("B123", "Sprite Zero", "12oz", 1.49, Brands.COCACOLA));
//    }

}

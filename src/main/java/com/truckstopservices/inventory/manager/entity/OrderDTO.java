package com.truckstopservices.inventory.manager.entity;

import com.truckstopservices.inventory.manager.config.Merchants;
import com.truckstopservices.inventory.merchandise.beverages.entity.Beverage;
import com.truckstopservices.inventory.merchandise.snacks.entity.CandyBar;
import com.truckstopservices.inventory.merchandise.snacks.entity.Chips;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    String orderID;
    Merchants company;
    double price;
    List<?> items;

    public OrderDTO(String orderID, Merchants company, double price, List<?> items) {
        this.orderID = orderID;
        this.company = company;
        this.price = price;
        this.items = items;
    }
}

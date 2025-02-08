package com.truckstopservices.inventory.restaurant.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class RestaurantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String skuCode;
    private String name;
    private int qty;
    private String size;
    private double price;

    public RestaurantModel(){}

    public RestaurantModel(String skuCode, String name, int qty, String size, double price) {
        this.skuCode = skuCode;
        this.name = name;
        this.qty = qty;
        this.size = size;
        this.price = price;

    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void reduceInventory(int qty){
        setQty(this.qty -= qty);
    }

    public void increaseInventory(int qty){
        setQty(this.qty += qty);
    }
}

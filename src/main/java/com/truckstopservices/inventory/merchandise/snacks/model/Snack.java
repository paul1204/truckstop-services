package com.truckstopservices.inventory.merchandise.snacks.model;

import com.truckstopservices.inventory.merchandise.config.Brands;

public abstract class Snack {
    public String skuCode;
    public String name;
    public double price;

    public Brands brand;

    int qty;


    public Snack(String skuCode, String name, double price, Brands brand, int qty) {
        this.skuCode = skuCode;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.qty = qty;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Brands getBrand() {
        return brand;
    }

    public void setBrand(Brands brand) {
        this.brand = brand;
    }
    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}

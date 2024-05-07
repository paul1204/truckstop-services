package com.truckstopservices.inventory.merchandise.snacks.model;

import com.truckstopservices.inventory.merchandise.config.Brands;

public abstract class Snack {
    public String skuCode;
    public String name;
    public double price;

    public Brands brand;

    public Snack(String skuCode, String name, double price, Brands brand) {
        this.skuCode = skuCode;
        this.name = name;
        this.price = price;
        this.brand = brand;
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
}

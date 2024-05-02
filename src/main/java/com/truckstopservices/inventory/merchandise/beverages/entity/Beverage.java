package com.truckstopservices.inventory.merchandise.beverages.entity;

import com.truckstopservices.inventory.merchandise.Brands;

public class Beverage {
    public String skuCode;
    public String name;
    public String size;
    public double price;

    public Brands brand;

    public Beverage(String skuCode, String name, String size, double price, Brands brand) {
        this.skuCode = skuCode;
        this.name = name;
        this.size = size;
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
}

package com.truckstopservices.inventory.merchandise.beverages.entity;

import com.truckstopservices.inventory.merchandise.config.Brands;

public class Beverage<T> {
    public String skuCode;
    public String name;
    public String size;
    public double price;

    public Brands brand;
    int qty;

    public Beverage(String skuCode, String name, String size, double price, Brands brand, int qty) {
        this.skuCode = skuCode;
        this.name = name;
        this.size = size;
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

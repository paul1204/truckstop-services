package com.truckstopservices.inventory.merchandise.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Consumable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String skuCode;

    private String name;

    private double price;

    private String brand;

    private int qty;

    private String size;

    public Consumable(String skuCode, String name, double price, String brand, int qty, String size) {
        this.skuCode = skuCode;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.qty = qty;
        this.size = size;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getSize() {return size;}

    public void setSize(String size) {this.size = size;}
}

package com.truckstopservices.inventory.merchandise.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@MappedSuperclass
public abstract class Consumable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String skuCode;

    private String name;

    private double costOfGoods;

    private double retailPrice;

    private String brand;

    private Double qty;

    private String size;

    private String merchandiseType;
    
    @ElementCollection
    private List<String> deliveryDates = new ArrayList<>();
    
    private String lastDeliveryDate;
    
    public Consumable(){}
    public Consumable(String skuCode, String name, double costOfGoods, String brand, double qty, String size) {
        this.skuCode = skuCode;
        this.name = name;
        this.costOfGoods = costOfGoods;
        this.brand = brand;
        this.qty = qty;
        this.size = size;
        this.retailPrice = Math.round(costOfGoods * 1.1 * 100.0) / 100.0;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {this.skuCode = skuCode;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCostOfGoods() {
        return costOfGoods;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setCostOfGoods(double costOfGoods) {
        this.costOfGoods = costOfGoods;
        this.retailPrice = Math.round(costOfGoods * 1.1 * 100.0) / 100.0;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getSize() {return size;}

    public void setSize(String size) {this.size = size;}

    public void reduceInventory(double qty){setQty(this.qty -= qty);}
    
    public void increaseInventory(double qty){this.qty += qty;}
    
    public List<String> getDeliveryDates() {
        return deliveryDates;
    }
    
    public void setDeliveryDates(List<String> deliveryDates) {
        this.deliveryDates = deliveryDates;
    }
    
    public String getLastDeliveryDate() {
        return lastDeliveryDate;
    }
    
    public void setLastDeliveryDate(String lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }
    
    public void addDelivery(String date) {
        if (deliveryDates == null) {
            deliveryDates = new ArrayList<>();
        }
        deliveryDates.add(date);
        lastDeliveryDate = date;
    }
}

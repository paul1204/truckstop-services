package com.truckstopservices.inventory.merchandise.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class DeliveryInfo {
    private String deliveryDate;
    private Double qtyOrdered;
    private Double costPerUnit;
    private Double retailPrice = 0.0;


    public DeliveryInfo() {}

    public DeliveryInfo(String deliveryDate, Double qtyOrdered, Double costPerUnit, Double retailPrice) {
        this.deliveryDate = deliveryDate;
        this.qtyOrdered = qtyOrdered;
        this.costPerUnit = costPerUnit;
        this.retailPrice = retailPrice;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Double getQtyOrdered() {
        return qtyOrdered;
    }

    public void setQtyOrdered(Double qtyOrdered) {
        this.qtyOrdered = qtyOrdered;
    }

    public Double getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(Double costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }
}
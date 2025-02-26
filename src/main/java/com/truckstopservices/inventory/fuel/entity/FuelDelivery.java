package com.truckstopservices.inventory.fuel.entity;

import jakarta.persistence.*;

@Entity
public class FuelDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String fuelDelivery_ID;
    private String deliveryDate;

    @OneToOne(cascade = CascadeType.ALL)
    private Diesel dieselOrder;
    @OneToOne(cascade = CascadeType.ALL)
    private RegularOctane regularOctaneOrder;
    @OneToOne(cascade = CascadeType.ALL)
    private PremiumOctane premiumOctaneOrder;

    //Mid Grade is a mixture of Premium and Regular Octane. No Mid Grade fuel order.
    //Determine Mid Grade calculations later

    public FuelDelivery() {}

    public FuelDelivery(String companyName, String fuelDeliveryID, String deliveryDate, Diesel dieselOrder,
                        RegularOctane regularOctaneOrder, PremiumOctane premiumOctaneOrder) {
        this.companyName = companyName;
        this.fuelDelivery_ID = fuelDeliveryID;
        this.deliveryDate = deliveryDate;
        this.dieselOrder = dieselOrder;
        this.regularOctaneOrder = regularOctaneOrder;
        this.premiumOctaneOrder = premiumOctaneOrder;
    }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFuelDelivery_ID() {
        return fuelDelivery_ID;
    }

    public void setFuelDelivery_ID(String fuelDelivery_ID) {
        this.fuelDelivery_ID = fuelDelivery_ID;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String date) {
        this.deliveryDate = date;
    }

    public Diesel getDieselOrder() {
        return dieselOrder;
    }

    public void setDieselOrder(Diesel dieselOrder) {
        this.dieselOrder = dieselOrder;
    }

    public RegularOctane getRegularOctaneOrder() {
        return regularOctaneOrder;
    }

    public void setRegularOctaneOrder(RegularOctane regularOctaneOrder) {
        this.regularOctaneOrder = regularOctaneOrder;
    }

    public PremiumOctane getPremiumOctaneOrder() {
        return premiumOctaneOrder;
    }

    public void setPremiumOctaneOrder(PremiumOctane premiumOctaneOrder) {
        this.premiumOctaneOrder = premiumOctaneOrder;
    }
}

package com.truckstopservices.processing.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

@Entity
public class MerchandiseSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long merchandiseSalesId;

    private double totalConvenienceStoreSales;

    @ManyToOne
    private ShiftReport shiftReport;

    //Break Down Convenience Store Items later...
//    private int coffee;
//    private int sodas;
//    private int waterBottles;
//    private int beer;
//    private int medication;
}

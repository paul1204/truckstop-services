package com.truckstopservices.processing.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

@Entity
public class TobaccoSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tobaccoSalesId;
    private double totalSales;


//    private int cigarettes;


}

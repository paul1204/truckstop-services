package com.truckstopservices.processing.entity;
import jakarta.persistence.*;

@Entity
public class TobaccoSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tobaccoSalesId;
    private double totalSales;

    @OneToOne
    private ShiftReport shiftReport;

//    private int cigarettes;


}

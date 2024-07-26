package com.truckstopservices.processing.entity;


import jakarta.persistence.*;

@Entity
public class RestaurantSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantSalesId;
    private double totalSales;

    @OneToOne
    private ShiftReport shiftReport;

//    private int breakfastMeals;
//    private int lunchMeals;
//    private int dinnerMeals;

}

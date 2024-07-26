package com.truckstopservices.processing.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
@Entity
public class RestaurantSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;
    private double totalSales;

    @ManyToOne
    private ShiftReport shiftReport;

//    private int breakfastMeals;
//    private int lunchMeals;
//    private int dinnerMeals;

}

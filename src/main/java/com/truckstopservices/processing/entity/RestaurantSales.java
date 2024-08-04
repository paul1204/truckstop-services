package com.truckstopservices.processing.entity;


import jakarta.persistence.*;

@Entity
public class RestaurantSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantSalesId;
    private double totalRestaurantSales;


    public void setShiftReport(ShiftReport shiftReport) {
        this.shiftReport = shiftReport;
    }

    @OneToOne
    //@JoinColumn(name = "shift_id", referencedColumnName = "shiftId")
    @JoinColumn(name = "shift_number", referencedColumnName = "shiftNumber")
    private ShiftReport shiftReport;


    public RestaurantSales(){}
    public RestaurantSales(double totalRestaurantSales) {
        this.totalRestaurantSales = totalRestaurantSales;
    }

    public double getRestaurantSalesSales() {
        return totalRestaurantSales;
    }

    public void setTotalRestaurantSalesSales(double totalSales) {
        this.totalRestaurantSales = totalSales;
    }
//    private int breakfastMeals;
//    private int lunchMeals;
//    private int dinnerMeals;

}

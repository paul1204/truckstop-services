package com.truckstopservices.processing.entity;


import jakarta.persistence.*;

@Entity
public class MerchandiseSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long merchandiseSalesId;

    private double merchandiseSales;

    @OneToOne
    //@JoinColumn(name = "shift_id", referencedColumnName = "shiftId")
    @JoinColumn(name = "shift_number", referencedColumnName = "shiftNumber")
    private ShiftReport shiftReport;

    public double getMerchandiseSales() {
        return merchandiseSales;
    }

    public void setMerchandiseSales(double merchandiseSales) {
        this.merchandiseSales = merchandiseSales;
    }

    public MerchandiseSales(){
    }
    public MerchandiseSales(double merchandiseSales) {
        this.merchandiseSales = merchandiseSales;
    }


    public void setShiftReport(ShiftReport shiftReport) {
        this.shiftReport = shiftReport;
    }
    //Break Down Convenience Store Items later...
//    private int coffee;
//    private int sodas;
//    private int waterBottles;
//    private int beer;
//    private int medication;
}

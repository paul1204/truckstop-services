package com.truckstopservices.processing.entity;
import jakarta.persistence.*;

@Entity
public class TobaccoSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tobaccoSalesId;
    private double tobaccoSales;

    @OneToOne
    //@JoinColumn(name = "shift_id", referencedColumnName = "shiftId")
    @JoinColumn(name = "shift_number", referencedColumnName = "shiftNumber")
    private ShiftReport shiftReport;


    public TobaccoSales(){}
    public TobaccoSales(double tobaccoSales) {
        this.tobaccoSales = tobaccoSales;
    }

    public double getTobaccoSales() {
        return tobaccoSales;
    }

    public void setTobaccoSales(double totalSales) {
        this.tobaccoSales = totalSales;
    }


    public void setShiftReport(ShiftReport shiftReport) {
        this.shiftReport = shiftReport;
    }
//    private int cigarettes;


}

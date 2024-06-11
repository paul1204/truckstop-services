package com.truckstopservices.processing.service;

import com.truckstopservices.posdataingest.model.ShiftReport;
import com.truckstopservices.processing.dto.ShiftReportDto;

public class ProcessingService {
    public ProcessingService(){

    }
    public ShiftReportDto parseData(ShiftReport posReport){
        ShiftReportDto shiftReportDto = new ShiftReportDto();
        shiftReportDto.setDate(posReport.getDate());
        shiftReportDto.setShiftNumber(posReport.getShiftNumber());
        shiftReportDto.setEmployeeID(posReport.getEmployeeID());
        shiftReportDto.setManagerID(posReport.getManagerID());
        shiftReportDto.setPosCashTil1(posReport.getPosCashTil1());
        shiftReportDto.setPosCashTil2(posReport.getPosCashTil2());

        shiftReportDto.setFuelSaleRegular(posReport.getFuelSaleRegular());
        shiftReportDto.setFuelSalesMidGrade(posReport.getFuelSalesMidGrade());
        shiftReportDto.setFuelSalesPremium(posReport.getFuelSalesPremium());
        shiftReportDto.setFuelSalesDiesel(posReport.getFuelSalesDiesel());
        shiftReportDto.setConvenienceStoreSales(posReport.getConvenienceStoreSales());
        shiftReportDto.setRestaurantSales(posReport.getRestaurantSales());
        shiftReportDto.setTobaccoSales(posReport.getTobaccoSales());

        return shiftReportDto;
    }

    public void pushToAccountingService(ShiftReport posReport){
        //make API call to Revenue Management Controller
    }
}

package com.truckstopservices.processing.service;

import com.truckstopservices.processing.entity.FuelSales;
import com.truckstopservices.processing.entity.ShiftReport;
import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.repository.ShiftReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

public class ProcessingService {
    @Autowired
    private ShiftReportRepository shiftReportRepository;

    @Transactional
    public ShiftReportDto parseData(ShiftReportDto shiftReportDto){
        ShiftReport shiftReport = shiftReportRepository.findByShiftNumber(shiftReportDto.shiftNumber())
                .orElse(new ShiftReport());
        shiftReport.setDate(shiftReportDto.date());
        shiftReport.setShiftNumber(shiftReportDto.shiftNumber());
        shiftReport.setEmployeeID(shiftReportDto.employeeID());
        shiftReport.setManagerID(shiftReportDto.managerID());
        shiftReport.setPosCashTil1(shiftReportDto.posCashTil1());
        shiftReport.setPosCashTil2(shiftReportDto.posCashTil2());

        Set<FuelSales> fuelSalesSet = new HashSet<>();
        FuelSales fuelSale = new FuelSales();
        //fuelSale.setRegularGasolineTransactions(shiftReportDto.fuelSaleRegular());
        return shiftReportDto;
    }

    public void pushToAccountingService(ShiftReport posReport){
        //make API call to Revenue Management Controller
    }
}

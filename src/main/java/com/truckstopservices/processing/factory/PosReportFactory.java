//package com.truckstopservices.processing.factory;
//
//import com.truckstopservices.processing.dto.ShiftReportDto;
//import com.truckstopservices.processing.model.PosReport;
//import com.truckstopservices.processing.model.ShiftReport;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PosReportFactory {
//
//    public PosReport createFromDto(ShiftReportDto shiftReportDto) {
//        return new ShiftReport(shiftReportDto.getDate(),shiftReportDto.getShiftNumber(),
//                shiftReportDto.getEmployeeID(),
//                shiftReportDto.getManagerID(),
//                shiftReportDto.getPosCashTil1(),
//                shiftReportDto.getPosCashTil2(),
//                shiftReportDto.getFuelSaleRegular(),
//                shiftReportDto.getFuelSalesMidGrade(),
//                shiftReportDto.getFuelSalesPremium(),
//                shiftReportDto.getFuelSalesDiesel(),
//                shiftReportDto.getConvenienceStoreSales(),
//                shiftReportDto.getRestaurantSales(),
//                shiftReportDto.getTobaccoSales());
//    }
//}

package com.truckstopservices.accounting.sales.controller;

import com.truckstopservices.accounting.sales.dto.SalesByShift;
import com.truckstopservices.accounting.sales.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    
    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/by-shift/{date}")
    public List<SalesByShift> findSalesByShift(@PathVariable("date") @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate date) {
        return salesService.findSalesByDate(date);
    }
}

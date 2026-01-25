package com.truckstopservices.accounting.sales.controller;

import com.truckstopservices.accounting.sales.dto.SalesByShift;
import com.truckstopservices.accounting.sales.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    
    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/by-shift")
    @CrossOrigin(origins = "http://localhost:8000")
    public List<SalesByShift> findSalesByTodayAllShifts() {
        return salesService.findSalesByTodayAllShifts();
    }
}

package com.truckstopservices.accounting.sales.service;

import com.truckstopservices.accounting.sales.dto.SalesByShift;
import com.truckstopservices.accounting.sales.entity.Sales;
import com.truckstopservices.accounting.sales.receipt.Receipt;
import com.truckstopservices.common.types.SalesType;
import com.truckstopservices.accounting.sales.repository.SalesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class SalesService {

    private static final Logger log = LoggerFactory.getLogger(SalesService.class);

    @Autowired
    private SalesRepository salesRepository;

//    @Autowired
//    private ReceiptV2Service receiptV2;

    public SalesService() {
    }

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public List<SalesByShift> findSalesByTodayAllShifts() {
        return salesRepository.findSalesByTodayAllShifts();
    }

    public Receipt createSalesReturnReceipt(double amount, SalesType salesType){
        LocalTime now = LocalTime.now();
        Integer shiftNumber = calculateShiftNumber(now);
        Sales sales = new Sales(LocalDate.now(), now, amount, salesType, shiftNumber);
        salesRepository.save(sales);
        log.info("Sales created with id: {}", sales.getSalesId());
        String receiptId = sales.getSalesId();
        return new Receipt(
                receiptId,
                sales.getSalesId(),
                SalesType.FUEL,
                amount
        );
    }

    private Integer calculateShiftNumber(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 0 && hour < 6) {
            return 1;
        } else if (hour >= 6 && hour < 12) {
            return 2;
        } else if (hour >= 12 && hour < 18) {
            return 3;
        } else {
            return 4;
        }
    }
}
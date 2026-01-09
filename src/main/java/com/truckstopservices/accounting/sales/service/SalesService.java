package com.truckstopservices.accounting.sales.service;

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

    public Receipt createSalesReturnReceipt(double amount, SalesType salesType){
        Sales sales = new Sales(LocalDate.now(), LocalTime.now(), amount, salesType);
        salesRepository.save(sales);
        String receiptId = sales.getSalesId();
        return new Receipt(
                receiptId,
                sales.getSalesId(),
                SalesType.FUEL,
                amount
        );
    }

//    public Receipt createSalesReturnReceipt(String date, double totalPrice, com.truckstopservices.accounting.receipt.enums.SalesType salesType) {
//    }
}
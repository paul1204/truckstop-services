package com.truckstopservices.accounting.sales.service;

import com.truckstopservices.posdataingest.model.POSSaleDto;
import com.truckstopservices.accounting.sales.dto.SalesByShift;
import com.truckstopservices.accounting.sales.entity.Sales;
import com.truckstopservices.accounting.sales.entity.SalesItem;
import com.truckstopservices.accounting.sales.receipt.Receipt;
import com.truckstopservices.common.types.SalesType;
import com.truckstopservices.accounting.sales.repository.SalesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesService {

    private static final Logger log = LoggerFactory.getLogger(SalesService.class);

    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public List<SalesByShift> findSalesByTodayAllShifts() {
        return salesRepository.findSalesByTodayAllShifts();
    }

    public Receipt createFuelSalesReturnReceipt(double amount, SalesType salesType, String fuelType, String terminal){
        LocalTime now = LocalTime.now();
        Integer shiftNumber = calculateShiftNumber(now);
        Sales sales = new Sales(LocalDate.now(), now, amount,shiftNumber, new ArrayList<>());
        sales.setTerminal(terminal != null ? terminal : "N/A Fuel Pump");
        SalesItem item = new SalesItem("Fuel", 1.00, 1.99, salesType, fuelType);
        sales.addSalesItem(item);
        salesRepository.save(sales);
        log.info("Fuel Sales created with id: {}", sales.getSalesId());
        String receiptId = sales.getSalesId();
        return new Receipt(
                receiptId,
                sales.getSalesId(),
                SalesType.FUEL,
                amount
        );
    }

    public Receipt createSaleFromPOS(POSSaleDto posSale){
        LocalTime now = LocalTime.now();
        Integer shiftNumber = calculateShiftNumber(now);
        Sales sales = new Sales(LocalDate.now(), now, posSale.totalSalesAmount(), shiftNumber, new ArrayList<>());
        sales.setTerminal(posSale.posTerminal() != null ? posSale.posTerminal() : "N/A POS");
        List<SalesItem> items = posSale.salesItems();
        if (items != null) {
            for (SalesItem item : items) {
                sales.addSalesItem(item);
            }
        }

        salesRepository.save(sales);
        String receiptId = sales.getSalesId();
        log.info("Sales created with id: {}", sales.getSalesId());
        return new Receipt(
                receiptId,
                sales.getSalesId(),
                null,
                posSale.totalSalesAmount()
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
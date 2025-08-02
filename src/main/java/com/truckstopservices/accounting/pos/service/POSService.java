package com.truckstopservices.accounting.pos.service;

import com.truckstopservices.accounting.pos.dto.Receipt;
import com.truckstopservices.accounting.pos.entity.POSDailyTable;
import com.truckstopservices.accounting.pos.enums.SalesType;
import com.truckstopservices.accounting.pos.repository.POSDailyTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class POSService {

    private final POSDailyTableRepository posDailyTableRepository;

    @Autowired
    public POSService(POSDailyTableRepository posDailyTableRepository) {
        this.posDailyTableRepository = posDailyTableRepository;
    }

    /**
     * Creates a new POS record for a fuel sale and returns a receipt
     * 
     * @param salesAmount The amount of the sale
     * @param salesType The type of sale (FUEL, MERCHANDISE, FOOD)
     * @return A Receipt object containing the transaction details
     */
    @Transactional
    public Receipt createPOSRecord(double salesAmount, SalesType salesType) {
        // Create a new POS record
        POSDailyTable posRecord = new POSDailyTable(
            LocalDateTime.now(),
            salesAmount,
            salesType
        );
        
        // Save the record to the database
        POSDailyTable savedRecord = posDailyTableRepository.save(posRecord);
        
        // Create and return a receipt
        return new Receipt(
            savedRecord.getReceiptId(),
            savedRecord.getSalesDate(),
            savedRecord.getSalesAmount(),
            savedRecord.getSalesType()
        );
    }
}
package com.truckstopservices.accounting.receipt.service;

import com.truckstopservices.accounting.receipt.dto.Receipt;
import com.truckstopservices.accounting.receipt.entity.ReceiptDailyTable;
import com.truckstopservices.accounting.receipt.enums.SalesType;
import com.truckstopservices.accounting.receipt.repository.ReceiptDailyTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReceiptService {

    private final ReceiptDailyTableRepository receiptDailyTableRepository;

    @Autowired
    public ReceiptService(ReceiptDailyTableRepository receiptDailyTableRepository) {
        this.receiptDailyTableRepository = receiptDailyTableRepository;
    }

    /**
     * Creates a new receipt record for a sale and returns a receipt DTO
     *
     * @param salesAmount The amount of the sale
     * @param salesType The type of sale (FUEL, MERCHANDISE, FOOD)
     * @return A Receipt object containing the transaction details
     */
    @Transactional
    public Receipt createReceiptRecord(double salesAmount, SalesType salesType) {
        // Create a new receipt record
        ReceiptDailyTable receiptRecord = new ReceiptDailyTable(
            LocalDateTime.now(),
            salesAmount,
            salesType
        );

        // Save the record to the database
        ReceiptDailyTable savedRecord = receiptDailyTableRepository.save(receiptRecord);

        // Create and return a receipt
        return new Receipt(
            savedRecord.getReceiptId(),
            savedRecord.getSalesDate(),
            savedRecord.getSalesAmount(),
            savedRecord.getSalesType()
        );
    }
}

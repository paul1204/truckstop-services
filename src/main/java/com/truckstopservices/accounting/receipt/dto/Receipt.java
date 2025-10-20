package com.truckstopservices.accounting.receipt.dto;

import com.truckstopservices.accounting.receipt.enums.SalesType;
import java.time.LocalDateTime;

public record Receipt(
    String receiptId,
    LocalDateTime salesDate,
    double salesAmount,
    SalesType salesType
) {
}
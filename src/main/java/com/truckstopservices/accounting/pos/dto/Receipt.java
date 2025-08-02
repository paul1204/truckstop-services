package com.truckstopservices.accounting.pos.dto;

import com.truckstopservices.accounting.pos.enums.SalesType;
import java.time.LocalDateTime;

public record Receipt(
    String receiptId,
    LocalDateTime salesDate,
    double salesAmount,
    SalesType salesType
) {
}
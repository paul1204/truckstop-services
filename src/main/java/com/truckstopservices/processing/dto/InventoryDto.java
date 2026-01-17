package com.truckstopservices.processing.dto;

import com.truckstopservices.common.types.SalesType;

public record InventoryDto(SalesType salesType, String skuCode, int qty) {
}

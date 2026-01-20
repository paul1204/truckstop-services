package com.truckstopservices.inventory.merchandise.dto;

public record ChartEntry(int id, String skuCode, String series, String date, double value, double retailPrice) {}
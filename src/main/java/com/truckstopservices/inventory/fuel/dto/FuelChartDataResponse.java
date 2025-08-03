package com.truckstopservices.inventory.fuel.dto;

public record FuelChartDataResponse(int id, String series, String group, double value) {
}
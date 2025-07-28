package com.truckstopservices.parking.dto;

import com.truckstopservices.parking.entity.ParkingRate.RateType;

public record ParkingResponse(
    Long id,
    String spotNumber,
    boolean occupied,
    String vehicleRegistration,
    RateType rateType,
    String occupiedSince,
    String reservedUntil,
    double totalCost,
    String message
) {
}
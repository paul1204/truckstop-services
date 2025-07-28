package com.truckstopservices.parking.dto;

import com.truckstopservices.parking.entity.ParkingRate.RateType;

public record ParkingRequest(
    String spotNumber,
    String vehicleRegistration,
    RateType rateType,
    String startTime,
    String endTime
) {
}
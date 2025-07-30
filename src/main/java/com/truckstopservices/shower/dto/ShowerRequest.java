package com.truckstopservices.shower.dto;

public record ShowerRequest(
    String showerNumber,
    String customerName,
    String startTime,
    String endTime
) {
}
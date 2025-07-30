package com.truckstopservices.shower.dto;

public record ShowerResponse(
    Long id,
    String showerNumber,
    boolean occupied,
    boolean cleaning,
    String customerName,
    String occupiedSince,
    String reservedUntil,
    String cleaningUntil,
    double totalCost,
    String message
) {
}
package com.truckstopservices.accounting.houseaccount.dto;

public record HouseAccountStatusDto(
        String houseAccountId,
        Double creditLimit,
        Double gallonsDue,
        Double availableGallons
) {
}

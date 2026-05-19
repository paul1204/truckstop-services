package com.truckstopservices.accounting.houseaccount.dto;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;

import java.time.LocalDateTime;

/**
 * DTO for house account responses.
 */
public record HouseAccountResponse(
        String houseAccountId, // Auto-generated UID
        String companyName,     // Business identifier
        String phoneNumber,
        String address,
        Double creditLimit,
        HouseAccount.AccountStanding accountStanding,
        Integer goodStandingDuration,
        Integer accountAge,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Double amountDue,
        Double gallonsDue
) {

    public static HouseAccountResponse fromEntity(HouseAccount houseAccount) {
        return new HouseAccountResponse(
                houseAccount.getHouseAccountId(),
                houseAccount.getCompanyName(),
                houseAccount.getPhoneNumber(),
                houseAccount.getAddress(),
                houseAccount.getCreditLimit(),
                houseAccount.getAccountStanding(),
                houseAccount.getGoodStandingDuration(),
                houseAccount.getAccountAge(),
                houseAccount.getCreatedAt(),
                houseAccount.getUpdatedAt(),
                houseAccount.getAmountDue(),
                houseAccount.getGallonsDue()
        );
    }
}
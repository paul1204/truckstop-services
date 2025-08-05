package com.truckstopservices.accounting.houseaccount.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HouseAccountTest {

    private HouseAccount houseAccount;

    @BeforeEach
    void setUp() {
        houseAccount = new HouseAccount("CUST123", "Test Company", "555-123-4567", "123 Main St");
    }

    @Test
    void constructor_SetsDefaultValues() {
        // Verify default values are set correctly
        assertEquals("CUST123", houseAccount.getCustomerNumber());
        assertEquals("Test Company", houseAccount.getName());
        assertEquals("555-123-4567", houseAccount.getPhoneNumber());
        assertEquals("123 Main St", houseAccount.getAddress());
        assertEquals(1000.0, houseAccount.getCreditLimit());
        assertEquals(HouseAccount.AccountStanding.GOOD, houseAccount.getAccountStanding());
        assertEquals(0, houseAccount.getGoodStandingDuration());
        assertEquals(0, houseAccount.getAccountAge());
        assertNotNull(houseAccount.getCreatedAt());
        assertNotNull(houseAccount.getUpdatedAt());
    }

    @Test
    void setAccountStanding_FromGoodToPastDue_ResetsGoodStandingDuration() {
        // Setup
        houseAccount.setGoodStandingDuration(30);
        
        // Execute
        houseAccount.setAccountStanding(HouseAccount.AccountStanding.PAST_DUE);
        
        // Verify
        assertEquals(HouseAccount.AccountStanding.PAST_DUE, houseAccount.getAccountStanding());
        assertEquals(0, houseAccount.getGoodStandingDuration());
    }

    @Test
    void setAccountStanding_FromGoodToOverDue_ResetsGoodStandingDuration() {
        // Setup
        houseAccount.setGoodStandingDuration(30);
        
        // Execute
        houseAccount.setAccountStanding(HouseAccount.AccountStanding.OVER_DUE);
        
        // Verify
        assertEquals(HouseAccount.AccountStanding.OVER_DUE, houseAccount.getAccountStanding());
        assertEquals(0, houseAccount.getGoodStandingDuration());
    }

    @Test
    void setAccountStanding_FromPastDueToGood_DoesNotResetGoodStandingDuration() {
        // Setup
        houseAccount.setAccountStanding(HouseAccount.AccountStanding.PAST_DUE);
        houseAccount.setGoodStandingDuration(0);
        LocalDateTime originalUpdatedAt = houseAccount.getUpdatedAt();
        
        // Wait to ensure timestamp would change
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Execute
        houseAccount.setAccountStanding(HouseAccount.AccountStanding.GOOD);
        
        // Verify
        assertEquals(HouseAccount.AccountStanding.GOOD, houseAccount.getAccountStanding());
        assertEquals(0, houseAccount.getGoodStandingDuration());
        assertTrue(houseAccount.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void incrementAccountAge_IncreasesAgeByOne() {
        // Setup
        int initialAge = houseAccount.getAccountAge();
        LocalDateTime originalUpdatedAt = houseAccount.getUpdatedAt();
        
        // Wait to ensure timestamp would change
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Execute
        houseAccount.incrementAccountAge();
        
        // Verify
        assertEquals(initialAge + 1, houseAccount.getAccountAge());
        assertTrue(houseAccount.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void incrementGoodStandingDuration_WhenGood_IncreasesGoodStandingDurationByOne() {
        // Setup
        houseAccount.setAccountStanding(HouseAccount.AccountStanding.GOOD);
        int initialDuration = houseAccount.getGoodStandingDuration();
        LocalDateTime originalUpdatedAt = houseAccount.getUpdatedAt();
        
        // Wait to ensure timestamp would change
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Execute
        houseAccount.incrementGoodStandingDuration();
        
        // Verify
        assertEquals(initialDuration + 1, houseAccount.getGoodStandingDuration());
        assertTrue(houseAccount.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void incrementGoodStandingDuration_WhenNotGood_DoesNotIncreaseGoodStandingDuration() {
        // Setup
        houseAccount.setAccountStanding(HouseAccount.AccountStanding.PAST_DUE);
        int initialDuration = houseAccount.getGoodStandingDuration();
        LocalDateTime originalUpdatedAt = houseAccount.getUpdatedAt();
        
        // Wait to ensure timestamp would change
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Execute
        houseAccount.incrementGoodStandingDuration();
        
        // Verify
        assertEquals(initialDuration, houseAccount.getGoodStandingDuration());
        assertTrue(houseAccount.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void amountDue_DefaultsToZero() {
        assertEquals(0.0, houseAccount.getAmountDue());
    }

    @Test
    void setAmountDue_UpdatesValue() {
        // Execute
        houseAccount.setAmountDue(500.0);
        
        // Verify
        assertEquals(500.0, houseAccount.getAmountDue());
    }
}
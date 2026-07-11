package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;
import com.truckstopservices.accounting.houseaccount.exception.HouseAccountException;
import com.truckstopservices.accounting.houseaccount.repository.HouseAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HouseAccountCreditLimitTest {

    @Mock
    private HouseAccountRepository houseAccountRepository;

    @InjectMocks
    private HouseAccountService houseAccountService;

    @Test
    void adjustBalance_ShouldThrowException_WhenCreditLimitExceeded() {
        // Arrange
        String accountId = "ACC123";
        HouseAccount account = new HouseAccount("Test Company", "555-0199", "123 Truck St");
        account.setHouseAccountId(accountId);
        account.setCreditLimit(1000.0);
        account.setAmountDue(900.0);
        account.setGallonsDue(850.0);
        
        when(houseAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        
        // Act & Assert
        HouseAccountException exception = assertThrows(HouseAccountException.class, () -> {
            houseAccountService.adjustBalance(accountId, 105.0, 20.0);
        });
        
        assertTrue(exception.getMessage().contains("Remaining credit: $150.0"));
        verify(houseAccountRepository, never()).save(any());
    }

    @Test
    void adjustBalance_ShouldSucceed_WhenWithinCreditLimit() {
        // Arrange
        String accountId = "ACC123";
        HouseAccount account = new HouseAccount("Test Company", "555-0199", "123 Truck St");
        account.setHouseAccountId(accountId);
        account.setCreditLimit(1000.0);
        account.setAmountDue(800.0);
        account.setGallonsDue(100.0);
        
        when(houseAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(houseAccountRepository.save(any(HouseAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        houseAccountService.adjustBalance(accountId, 150.0, 30.0);
        
        // Assert
        assertEquals(950.0, account.getAmountDue());
        assertEquals(130.0, account.getGallonsDue());
        verify(houseAccountRepository, times(1)).save(account);
    }
}

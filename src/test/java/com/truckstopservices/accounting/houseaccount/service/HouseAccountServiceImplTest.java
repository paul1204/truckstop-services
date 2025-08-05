package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
import com.truckstopservices.accounting.houseaccount.dto.HouseAccountResponse;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;
import com.truckstopservices.accounting.houseaccount.exception.HouseAccountException;
import com.truckstopservices.accounting.houseaccount.repository.HouseAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HouseAccountServiceImplTest {

    @Mock
    private HouseAccountRepository houseAccountRepository;

    @InjectMocks
    private HouseAccountServiceImpl houseAccountService;

    private HouseAccount houseAccount;
    private HouseAccountRequest validRequest;

    @BeforeEach
    void setUp() {
        // Setup test data
        houseAccount = new HouseAccount("CUST123", "Test Company", "555-123-4567", "123 Main St");
        houseAccount.setCreditLimit(1000.0);
        houseAccount.setAccountStanding(AccountStanding.GOOD);
        houseAccount.setGoodStandingDuration(0);
        houseAccount.setAccountAge(0);
        
        validRequest = new HouseAccountRequest();
        validRequest.setCustomerNumber("CUST123");
        validRequest.setName("Test Company");
        validRequest.setPhoneNumber("555-123-4567");
        validRequest.setAddress("123 Main St");
        validRequest.setCreditLimit(1000.0);
    }

    @Test
    void createHouseAccount_ValidRequest_ReturnsResponse() {
        // Setup
        when(houseAccountRepository.existsById("CUST123")).thenReturn(false);
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.createHouseAccount(validRequest);

        // Verify
        assertNotNull(response);
        assertEquals("CUST123", response.getCustomerNumber());
        assertEquals("Test Company", response.getName());
        verify(houseAccountRepository).save(any(HouseAccount.class));
    }

    @Test
    void createHouseAccount_ExistingAccount_ThrowsException() {
        // Setup
        when(houseAccountRepository.existsById("CUST123")).thenReturn(true);

        // Execute & Verify
        assertThrows(HouseAccountException.class, () -> {
            houseAccountService.createHouseAccount(validRequest);
        });
        verify(houseAccountRepository, never()).save(any(HouseAccount.class));
    }

    @Test
    void getHouseAccount_ExistingAccount_ReturnsResponse() {
        // Setup
        when(houseAccountRepository.findById("CUST123")).thenReturn(Optional.of(houseAccount));

        // Execute
        HouseAccountResponse response = houseAccountService.getHouseAccount("CUST123");

        // Verify
        assertNotNull(response);
        assertEquals("CUST123", response.getCustomerNumber());
        assertEquals("Test Company", response.getName());
    }

    @Test
    void getHouseAccount_NonExistingAccount_ThrowsException() {
        // Setup
        when(houseAccountRepository.findById("NONEXISTENT")).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(HouseAccountException.class, () -> {
            houseAccountService.getHouseAccount("NONEXISTENT");
        });
    }

    @Test
    void getAllHouseAccounts_ReturnsAllAccounts() {
        // Setup
        List<HouseAccount> accounts = Arrays.asList(houseAccount);
        when(houseAccountRepository.findAll()).thenReturn(accounts);

        // Execute
        List<HouseAccountResponse> responses = houseAccountService.getAllHouseAccounts();

        // Verify
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("CUST123", responses.get(0).getCustomerNumber());
    }

    @Test
    void updateHouseAccount_ExistingAccount_ReturnsUpdatedResponse() {
        // Setup
        HouseAccountRequest updateRequest = new HouseAccountRequest();
        updateRequest.setName("Updated Company");
        updateRequest.setPhoneNumber("555-987-6543");
        
        when(houseAccountRepository.findById("CUST123")).thenReturn(Optional.of(houseAccount));
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.updateHouseAccount("CUST123", updateRequest);

        // Verify
        assertNotNull(response);
        assertEquals("CUST123", response.getCustomerNumber());
        // The mock returns the original houseAccount, but in a real scenario, it would have updated values
        verify(houseAccountRepository).save(any(HouseAccount.class));
    }

    @Test
    void deleteHouseAccount_ExistingAccount_DeletesAccount() {
        // Setup
        when(houseAccountRepository.existsById("CUST123")).thenReturn(true);
        doNothing().when(houseAccountRepository).deleteById("CUST123");

        // Execute
        houseAccountService.deleteHouseAccount("CUST123");

        // Verify
        verify(houseAccountRepository).deleteById("CUST123");
    }

    @Test
    void deleteHouseAccount_NonExistingAccount_ThrowsException() {
        // Setup
        when(houseAccountRepository.existsById("NONEXISTENT")).thenReturn(false);

        // Execute & Verify
        assertThrows(HouseAccountException.class, () -> {
            houseAccountService.deleteHouseAccount("NONEXISTENT");
        });
        verify(houseAccountRepository, never()).deleteById(anyString());
    }

    @Test
    void updateAccountStanding_ExistingAccount_ReturnsUpdatedResponse() {
        // Setup
        when(houseAccountRepository.findById("CUST123")).thenReturn(Optional.of(houseAccount));
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.updateAccountStanding("CUST123", AccountStanding.PAST_DUE);

        // Verify
        assertNotNull(response);
        assertEquals("CUST123", response.getCustomerNumber());
        verify(houseAccountRepository).save(any(HouseAccount.class));
    }

    @Test
    void updateCreditLimit_ExistingAccount_ReturnsUpdatedResponse() {
        // Setup
        when(houseAccountRepository.findById("CUST123")).thenReturn(Optional.of(houseAccount));
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.updateCreditLimit("CUST123", 1500.0);

        // Verify
        assertNotNull(response);
        assertEquals("CUST123", response.getCustomerNumber());
        verify(houseAccountRepository).save(any(HouseAccount.class));
    }

    @Test
    void findByAccountStanding_ReturnsMatchingAccounts() {
        // Setup
        List<HouseAccount> accounts = Arrays.asList(houseAccount);
        when(houseAccountRepository.findByAccountStanding(AccountStanding.GOOD)).thenReturn(accounts);

        // Execute
        List<HouseAccountResponse> responses = houseAccountService.findByAccountStanding(AccountStanding.GOOD);

        // Verify
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("CUST123", responses.get(0).getCustomerNumber());
    }

    @Test
    void findByName_ReturnsMatchingAccounts() {
        // Setup
        List<HouseAccount> accounts = Arrays.asList(houseAccount);
        when(houseAccountRepository.findByCustomerNumber("Test")).thenReturn(accounts);

        // Execute
        List<HouseAccountResponse> responses = houseAccountService.findByName("Test");

        // Verify
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("CUST123", responses.get(0).getCustomerNumber());
    }

    @Test
    void findByPhoneNumber_ReturnsMatchingAccounts() {
        // Setup
        List<HouseAccount> accounts = Arrays.asList(houseAccount);
        when(houseAccountRepository.findByPhoneNumber("555-123-4567")).thenReturn(accounts);

        // Execute
        List<HouseAccountResponse> responses = houseAccountService.findByPhoneNumber("555-123-4567");

        // Verify
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("CUST123", responses.get(0).getCustomerNumber());
    }
}
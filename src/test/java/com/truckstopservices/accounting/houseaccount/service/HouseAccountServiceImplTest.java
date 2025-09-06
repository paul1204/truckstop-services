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
        houseAccount = new HouseAccount("CUST123", "555-123-4567", "123 Main St");
        houseAccount.setHouseAccountId("test-id-123"); // Set a test ID
        houseAccount.setCreditLimit(1000.0);
        houseAccount.setAccountStanding(AccountStanding.GOOD);
        houseAccount.setGoodStandingDuration(0);
        houseAccount.setAccountAge(0);
        
        validRequest = new HouseAccountRequest();
        validRequest.setCompanyName("CUST123");
        validRequest.setPhoneNumber("555-123-4567");
        validRequest.setAddress("123 Main St");
        validRequest.setCreditLimit(1000.0);
    }

    @Test
    void createHouseAccount_ValidRequest_ReturnsResponse() {
        // Setup
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.createHouseAccount(validRequest);

        // Verify
        assertNotNull(response);
        assertEquals("test-id-123", response.getHouseAccountId());
        assertEquals("CUST123", response.getCompanyName());
        verify(houseAccountRepository).save(any(HouseAccount.class));
    }

    @Test
    void createHouseAccount_WithCompanyName_ReturnsResponse() {
        // Setup
        validRequest.setCompanyName("Company456");
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.createHouseAccount(validRequest);

        // Verify
        assertNotNull(response);
        assertEquals("test-id-123", response.getHouseAccountId());
        assertEquals("CUST123", response.getCompanyName()); // This will be CUST123 because we're returning the mock houseAccount
        verify(houseAccountRepository).save(any(HouseAccount.class));
    }

    @Test
    void getHouseAccount_ExistingAccount_ReturnsResponse() {
        // Setup
        when(houseAccountRepository.findById("test-id-123")).thenReturn(Optional.of(houseAccount));

        // Execute
        HouseAccountResponse response = houseAccountService.getHouseAccount("test-id-123");

        // Verify
        assertNotNull(response);
        assertEquals("test-id-123", response.getHouseAccountId());
        assertEquals("CUST123", response.getCompanyName());
    }

    @Test
    void getHouseAccount_NonExistingAccount_ThrowsException() {
        // Setup
        when(houseAccountRepository.findById("nonexistent-id")).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(HouseAccountException.class, () -> {
            houseAccountService.getHouseAccount("nonexistent-id");
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
        assertEquals("test-id-123", responses.get(0).getHouseAccountId());
        assertEquals("CUST123", responses.get(0).getCompanyName());
    }

    @Test
    void updateHouseAccount_ExistingAccount_ReturnsUpdatedResponse() {
        // Setup
        HouseAccountRequest updateRequest = new HouseAccountRequest();
        updateRequest.setCompanyName("Updated Company");
        updateRequest.setPhoneNumber("555-987-6543");
        
        when(houseAccountRepository.findById("test-id-123")).thenReturn(Optional.of(houseAccount));
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.updateHouseAccount("test-id-123", updateRequest);

        // Verify
        assertNotNull(response);
        assertEquals("test-id-123", response.getHouseAccountId());
        assertEquals("Updated Company", response.getCompanyName());
        // The mock returns the original houseAccount, but in a real scenario, it would have updated values
        verify(houseAccountRepository).save(any(HouseAccount.class));
    }

    @Test
    void deleteHouseAccount_ExistingAccount_DeletesAccount() {
        // Setup
        when(houseAccountRepository.existsById("test-id-123")).thenReturn(true);
        doNothing().when(houseAccountRepository).deleteById("test-id-123");

        // Execute
        houseAccountService.deleteHouseAccount("test-id-123");

        // Verify
        verify(houseAccountRepository).deleteById("test-id-123");
    }

    @Test
    void deleteHouseAccount_NonExistingAccount_ThrowsException() {
        // Setup
        when(houseAccountRepository.existsById("nonexistent-id")).thenReturn(false);

        // Execute & Verify
        assertThrows(HouseAccountException.class, () -> {
            houseAccountService.deleteHouseAccount("nonexistent-id");
        });
        verify(houseAccountRepository, never()).deleteById(anyString());
    }

    @Test
    void updateAccountStanding_ExistingAccount_ReturnsUpdatedResponse() {
        // Setup
        when(houseAccountRepository.findById("test-id-123")).thenReturn(Optional.of(houseAccount));
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.updateAccountStanding("test-id-123", AccountStanding.PAST_DUE);

        // Verify
        assertNotNull(response);
        assertEquals("test-id-123", response.getHouseAccountId());
        assertEquals("CUST123", response.getCompanyName());
        verify(houseAccountRepository).save(any(HouseAccount.class));
    }

    @Test
    void updateCreditLimit_ExistingAccount_ReturnsUpdatedResponse() {
        // Setup
        when(houseAccountRepository.findById("test-id-123")).thenReturn(Optional.of(houseAccount));
        when(houseAccountRepository.save(any(HouseAccount.class))).thenReturn(houseAccount);

        // Execute
        HouseAccountResponse response = houseAccountService.updateCreditLimit("test-id-123", 1500.0);

        // Verify
        assertNotNull(response);
        assertEquals("test-id-123", response.getHouseAccountId());
        assertEquals("CUST123", response.getCompanyName());
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
        assertEquals("test-id-123", responses.get(0).getHouseAccountId());
        assertEquals("CUST123", responses.get(0).getCompanyName());
    }

    @Test
    void findByName_ReturnsMatchingAccounts() {
        // Setup
        List<HouseAccount> accounts = Arrays.asList(houseAccount);
        when(houseAccountRepository.findByCompanyName("Test")).thenReturn(accounts);

        // Execute
        List<HouseAccountResponse> responses = houseAccountService.findByName("Test");

        // Verify
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("test-id-123", responses.get(0).getHouseAccountId());
        assertEquals("CUST123", responses.get(0).getCompanyName());
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
        assertEquals("test-id-123", responses.get(0).getHouseAccountId());
        assertEquals("CUST123", responses.get(0).getCompanyName());
    }
}
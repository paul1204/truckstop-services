package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
import com.truckstopservices.accounting.houseaccount.dto.HouseAccountResponse;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;
import com.truckstopservices.accounting.houseaccount.repository.HouseAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HouseAccountServiceTest {

    @Mock
    private HouseAccountRepository houseAccountRepository;

    @InjectMocks
    private HouseAccountService houseAccountService;

    @Test
    void createHouseAccount_SimpleTest() {
        HouseAccount account = new HouseAccount("Test Co", "123", "Addr");
        account.setHouseAccountId("ID1");
        when(houseAccountRepository.save(any())).thenReturn(account);

        HouseAccountRequest request = new HouseAccountRequest("Test Co", "123", "Addr", 1000.0);
        HouseAccountResponse response = houseAccountService.createHouseAccount(request);

        assertNotNull(response);
        assertEquals("ID1", response.getHouseAccountId());
    }

    @Test
    void getHouseAccount_SimpleTest() {
        HouseAccount account = new HouseAccount("Test Co", "123", "Addr");
        account.setHouseAccountId("ID1");
        when(houseAccountRepository.findById("ID1")).thenReturn(Optional.of(account));

        HouseAccountResponse response = houseAccountService.getHouseAccount("ID1");

        assertNotNull(response);
        assertEquals("ID1", response.getHouseAccountId());
    }
}
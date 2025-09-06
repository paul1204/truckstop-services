package com.truckstopservices.accounting.houseaccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
import com.truckstopservices.accounting.houseaccount.dto.HouseAccountResponse;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;
import com.truckstopservices.accounting.houseaccount.exception.HouseAccountException;
import com.truckstopservices.accounting.houseaccount.service.HouseAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HouseAccountController.class)
public class HouseAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HouseAccountService houseAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    private HouseAccountRequest validRequest;
    private HouseAccountResponse mockResponse;
    private List<HouseAccountResponse> mockResponses;

    @BeforeEach
    void setUp() {
        // Setup test data
        validRequest = new HouseAccountRequest("Test Company", "555-123-4567", "123 Main St", 1000.0);
        
        mockResponse = new HouseAccountResponse();
        mockResponse.setHouseAccountId("CUST123");
        mockResponse.setCompanyName("Test Company");
        mockResponse.setPhoneNumber("555-123-4567");
        mockResponse.setAddress("123 Main St");
        mockResponse.setCreditLimit(1000.0);
        mockResponse.setAccountStanding(AccountStanding.GOOD);
        mockResponse.setGoodStandingDuration(0);
        mockResponse.setAccountAge(0);
        
        mockResponses = Arrays.asList(mockResponse);
    }

    @Test
    void createHouseAccount_ValidRequest_ReturnsCreated() throws Exception {
        when(houseAccountService.createHouseAccount(any(HouseAccountRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/accounting/house-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.houseAccountId").value("CUST123"))
                .andExpect(jsonPath("$.companyName").value("Test Company"));
    }

    @Test
    void getHouseAccount_ExistingAccount_ReturnsOk() throws Exception {
        when(houseAccountService.getHouseAccount("CUST123")).thenReturn(mockResponse);

        mockMvc.perform(get("/accounting/house-accounts/CUST123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.houseAccountId").value("CUST123"))
                .andExpect(jsonPath("$.companyName").value("Test Company"));
    }

    @Test
    void getHouseAccount_NonExistingAccount_ReturnsNotFound() throws Exception {
        when(houseAccountService.getHouseAccount("NONEXISTENT")).thenThrow(new HouseAccountException("Not found"));

        mockMvc.perform(get("/accounting/house-accounts/NONEXISTENT"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllHouseAccounts_ReturnsOk() throws Exception {
        when(houseAccountService.getAllHouseAccounts()).thenReturn(mockResponses);

        mockMvc.perform(get("/accounting/house-accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houseAccountId").value("CUST123"))
                .andExpect(jsonPath("$[0].companyName").value("Test Company"));
    }

//    @Test
//    void updateHouseAccount_ExistingAccount_ReturnsOk() throws Exception {
//        when(houseAccountService.updateHouseAccount(eq("CUST123"), any(HouseAccountRequest.class))).thenReturn(mockResponse);
//
//        mockMvc.perform(put("/accounting/house-accounts/CUST123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(validRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.customerNumber").value("CUST123"))
//                .andExpect(jsonPath("$.name").value("Test Company"));
//    }

    @Test
    void deleteHouseAccount_ExistingAccount_ReturnsNoContent() throws Exception {
        doNothing().when(houseAccountService).deleteHouseAccount("CUST123");

        mockMvc.perform(delete("/accounting/house-accounts/CUST123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateAccountStanding_ValidStanding_ReturnsOk() throws Exception {
        when(houseAccountService.updateAccountStanding(eq("CUST123"), eq(AccountStanding.PAST_DUE))).thenReturn(mockResponse);

        mockMvc.perform(patch("/accounting/house-accounts/CUST123/standing")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"standing\": \"PAST_DUE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.houseAccountId").value("CUST123"));
    }

    @Test
    void updateCreditLimit_ExistingAccount_ReturnsOk() throws Exception {
        when(houseAccountService.updateCreditLimit(eq("CUST123"), eq(1500.0))).thenReturn(mockResponse);

        mockMvc.perform(patch("/accounting/house-accounts/CUST123/credit-limit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"creditLimit\": 1500.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.houseAccountId").value("CUST123"));
    }
    
    @Test
    void findByAccountStanding_ValidStanding_ReturnsOk() throws Exception {
        when(houseAccountService.findByAccountStanding(AccountStanding.GOOD)).thenReturn(mockResponses);

        mockMvc.perform(get("/accounting/house-accounts/standing/GOOD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houseAccountId").value("CUST123"));
    }
    
    @Test
    void findByAccountStanding_InvalidStanding_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/accounting/house-accounts/standing/INVALID"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void findByCustomerName_ExactCustomerNumber_ReturnsOk() throws Exception {
        when(houseAccountService.findByName("CUST123")).thenReturn(mockResponses);
        
        mockMvc.perform(get("/accounting/house-accounts/search/CUST123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houseAccountId").value("CUST123"));
    }
    
    @Test
    void findByCustomerName_PartialName_ReturnsOk() throws Exception {
        when(houseAccountService.getHouseAccount("Test")).thenThrow(new HouseAccountException("Not found"));
        when(houseAccountService.findByName("Test")).thenReturn(mockResponses);
        
        mockMvc.perform(get("/accounting/house-accounts/search/Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houseAccountId").value("CUST123"));
    }
    
    @Test
    void findByPhoneNumber_ValidPhoneNumber_ReturnsOk() throws Exception {
        when(houseAccountService.findByPhoneNumber("555-123-4567")).thenReturn(mockResponses);
        
        mockMvc.perform(get("/accounting/house-accounts/phone/555-123-4567"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houseAccountId").value("CUST123"));
    }
}
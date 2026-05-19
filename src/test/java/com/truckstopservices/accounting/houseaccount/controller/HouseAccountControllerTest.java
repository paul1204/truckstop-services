//package com.truckstopservices.accounting.houseaccount.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
//import com.truckstopservices.accounting.houseaccount.dto.HouseAccountResponse;
//import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;
//import com.truckstopservices.accounting.houseaccount.service.HouseAccountService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(HouseAccountController.class)
//public class HouseAccountControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private HouseAccountService houseAccountService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void createHouseAccount_SimpleTest() throws Exception {
//        HouseAccountRequest request = new HouseAccountRequest("Test Co", "123", "Addr", 1000.0);
//        HouseAccountResponse response = new HouseAccountResponse();
//        response.setHouseAccountId("ID1");
//        response.setCompanyName("Test Co");
//
//        when(houseAccountService.createHouseAccount(any())).thenReturn(response);
//
//        mockMvc.perform(post("/accounting/house-accounts")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.houseAccountId").value("ID1"));
//    }
//
//    @Test
//    void getHouseAccount_SimpleTest() throws Exception {
//        HouseAccountResponse response = new HouseAccountResponse();
//        response.setHouseAccountId("ID1");
//
//        when(houseAccountService.getHouseAccount("ID1")).thenReturn(response);
//
//        mockMvc.perform(get("/accounting/house-accounts/ID1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.houseAccountId").value("ID1"));
//    }
//
//    @Test
//    void getAllHouseAccounts_SimpleTest() throws Exception {
//        when(houseAccountService.getAllHouseAccounts()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/accounting/house-accounts"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void deleteHouseAccount_SimpleTest() throws Exception {
//        mockMvc.perform(delete("/accounting/house-accounts/ID1"))
//                .andExpect(status().isNoContent());
//    }
//}
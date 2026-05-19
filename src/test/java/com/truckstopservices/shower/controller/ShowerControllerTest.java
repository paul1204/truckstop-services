//package com.truckstopservices.shower.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.truckstopservices.shower.dto.ShowerRequest;
//import com.truckstopservices.shower.dto.ShowerResponse;
//import com.truckstopservices.shower.service.ShowerService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ShowerController.class)
//public class ShowerControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private ShowerService showerService;
//
//    @Test
//    void getAllShowers_SimpleTest() throws Exception {
//        when(showerService.getAllShowers()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/showers"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void getShowerByNumber_SimpleTest() throws Exception {
//         ShowerResponse response = new ShowerResponse(1L, "S01", false, false, null, null, null, null, 0.0, "OK");
//
//        when(showerService.getShowerByNumber("S01")).thenReturn(response);
//
//        mockMvc.perform(get("/api/showers/S01"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.showerNumber").value("S01"));
//    }
//
//    @Test
//    void createShowerReservation_SimpleTest() throws Exception {
//        ShowerRequest request = new ShowerRequest("S01", "John", null, null);
//        ShowerResponse response = new ShowerResponse(1L, "S01", true, false, "John", null, null, null, 15.0, "OK");
//
//        when(showerService.createShowerReservation(any())).thenReturn(response);
//
//        mockMvc.perform(post("/api/showers/reserve")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void releaseShower_SimpleTest() throws Exception {
//        ShowerResponse response = new ShowerResponse(1L, "S01", false, true, "John", null, null, null, 0.0, "OK");
//
//        when(showerService.releaseShower("S01")).thenReturn(response);
//
//        mockMvc.perform(put("/api/showers/release/S01"))
//                .andExpect(status().isOk());
//    }
//}

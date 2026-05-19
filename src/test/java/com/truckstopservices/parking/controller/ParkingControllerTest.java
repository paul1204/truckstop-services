//package com.truckstopservices.parking.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.truckstopservices.parking.dto.ParkingRequest;
//import com.truckstopservices.parking.dto.ParkingResponse;
//import com.truckstopservices.parking.entity.ParkingRate.RateType;
//import com.truckstopservices.parking.service.ParkingService;
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
//@WebMvcTest(ParkingController.class)
//public class ParkingControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private ParkingService parkingService;
//
//    @Test
//    void getAllParkingSpots_SimpleTest() throws Exception {
//        when(parkingService.getAllParkingSpots()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/parking/spots"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void getParkingSpotByNumber_SimpleTest() throws Exception {
//        ParkingResponse response = new ParkingResponse(1L, "A01", false, null, null, null, null, 0.0, "OK");
//
//        when(parkingService.getParkingSpotByNumber("A01")).thenReturn(response);
//
//        mockMvc.perform(get("/api/parking/spots/A01"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.spotNumber").value("A01"));
//    }
//
//    @Test
//    void createParkingSpotReservation_SimpleTest() throws Exception {
//        ParkingRequest request = new ParkingRequest("A01", "XYZ789", RateType.HOURLY, null, null);
//        ParkingResponse response = new ParkingResponse(1L, "A01", true, "XYZ789", RateType.HOURLY, null, null, 17.97, "OK");
//
//        when(parkingService.createParkingSpotReservation(any())).thenReturn(response);
//
//        mockMvc.perform(post("/api/parking/reserve")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void releaseParkingSpot_SimpleTest() throws Exception {
//        ParkingResponse response = new ParkingResponse(1L, "A01", false, "XYZ789", null, null, null, 0.0, "OK");
//
//        when(parkingService.releaseParkingSpot("A01")).thenReturn(response);
//
//        mockMvc.perform(put("/api/parking/release/A01"))
//                .andExpect(status().isOk());
//    }
//}
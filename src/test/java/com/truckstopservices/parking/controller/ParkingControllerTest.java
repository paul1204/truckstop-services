package com.truckstopservices.parking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.truckstopservices.parking.dto.ParkingRequest;
import com.truckstopservices.parking.dto.ParkingResponse;
import com.truckstopservices.parking.entity.ParkingRate.RateType;
import com.truckstopservices.parking.exception.ParkingException;
import com.truckstopservices.parking.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParkingController.class)
public class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParkingService parkingService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private String startTime;
    private String endTime;
    private ParkingResponse parkingResponse1;
    private ParkingResponse parkingResponse2;
    private ParkingRequest parkingRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        startTime = now.format(formatter);
        endTime = now.plusHours(3).format(formatter);

        parkingResponse1 = new ParkingResponse(
            1L, "A01", false, null, null, null, null, 0.0, ""
        );

        parkingResponse2 = new ParkingResponse(
            2L, "A02", true, "ABC123", RateType.HOURLY, startTime, endTime, 17.97, "Occupied"
        );

        parkingRequest = new ParkingRequest(
            "A01", "XYZ789", RateType.HOURLY, startTime, endTime
        );
    }

    @Test
    void getAllParkingSpots_ShouldReturnAllSpots() throws Exception {
        List<ParkingResponse> responses = Arrays.asList(parkingResponse1, parkingResponse2);
        when(parkingService.getAllParkingSpots()).thenReturn(responses);

        mockMvc.perform(get("/api/parking/spots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].spotNumber", is("A01")))
                .andExpect(jsonPath("$[0].occupied", is(false)))
                .andExpect(jsonPath("$[1].spotNumber", is("A02")))
                .andExpect(jsonPath("$[1].occupied", is(true)))
                .andExpect(jsonPath("$[1].vehicleRegistration", is("ABC123")));
    }

    @Test
    void getAvailableParkingSpots_ShouldReturnOnlyAvailableSpots() throws Exception {
        List<ParkingResponse> responses = Arrays.asList(parkingResponse1);
        when(parkingService.getAvailableParkingSpots()).thenReturn(responses);

        mockMvc.perform(get("/api/parking/spots/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].spotNumber", is("A01")))
                .andExpect(jsonPath("$[0].occupied", is(false)));
    }

    @Test
    void getParkingSpotByNumber_WhenSpotExists_ShouldReturnSpot() throws Exception {
        when(parkingService.getParkingSpotByNumber("A01")).thenReturn(parkingResponse1);

        mockMvc.perform(get("/api/parking/spots/A01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spotNumber", is("A01")))
                .andExpect(jsonPath("$.occupied", is(false)));
    }

    @Test
    void getParkingSpotByNumber_WhenSpotDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(parkingService.getParkingSpotByNumber("A99")).thenThrow(new ParkingException("Parking spot not found"));

        mockMvc.perform(get("/api/parking/spots/A99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createParkingSpotReservation_WhenSpotIsAvailable_ShouldCreateReservation() throws Exception {
        ParkingResponse response = new ParkingResponse(
            1L, "A01", true, "XYZ789", RateType.HOURLY, startTime, endTime, 17.97, 
            "Parking spot reservation created successfully"
        );
        
        when(parkingService.createParkingSpotReservation(any(ParkingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/parking/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parkingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.spotNumber", is("A01")))
                .andExpect(jsonPath("$.occupied", is(true)))
                .andExpect(jsonPath("$.vehicleRegistration", is("XYZ789")))
                .andExpect(jsonPath("$.rateType", is("HOURLY")))
                .andExpect(jsonPath("$.message", is("Parking spot reservation created successfully")));
    }

    @Test
    void createParkingSpotReservation_WhenRateTypeDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(parkingService.createParkingSpotReservation(any(ParkingRequest.class)))
                .thenThrow(new ParkingException("Rate not found"));

        mockMvc.perform(post("/api/parking/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parkingRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void releaseParkingSpot_WhenSpotIsOccupied_ShouldReleaseSpot() throws Exception {
        ParkingResponse response = new ParkingResponse(
            2L, "A02", false, "ABC123", null, startTime, endTime, 0.0, 
            "Parking spot released successfully"
        );
        
        when(parkingService.releaseParkingSpot("A02")).thenReturn(response);

        mockMvc.perform(put("/api/parking/release/A02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spotNumber", is("A02")))
                .andExpect(jsonPath("$.occupied", is(false)))
                .andExpect(jsonPath("$.vehicleRegistration", is("ABC123")))
                .andExpect(jsonPath("$.message", is("Parking spot released successfully")));
    }

    @Test
    void releaseParkingSpot_WhenSpotDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(parkingService.releaseParkingSpot("A99")).thenThrow(new ParkingException("Parking spot not found"));

        mockMvc.perform(put("/api/parking/release/A99"))
                .andExpect(status().isNotFound());
    }
}
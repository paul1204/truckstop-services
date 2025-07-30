package com.truckstopservices.shower.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.truckstopservices.shower.dto.ShowerRequest;
import com.truckstopservices.shower.dto.ShowerResponse;
import com.truckstopservices.shower.exception.ShowerException;
import com.truckstopservices.shower.repository.ShowerRepository;
import com.truckstopservices.shower.repository.ShowerRateRepository;
import com.truckstopservices.shower.service.ShowerService;
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

@WebMvcTest(ShowerController.class)
public class ShowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShowerService showerService;

    @MockBean
    private ShowerRepository showerRepository;

    @MockBean
    private ShowerRateRepository showerRateRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private String startTime;
    private String endTime;
    private ShowerResponse showerResponse1;
    private ShowerResponse showerResponse2;
    private ShowerRequest showerRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        startTime = now.format(formatter);
        endTime = now.plusHours(1).format(formatter);

        showerResponse1 = new ShowerResponse(
            1L, "S01", false, false, null, null, null, null, 0.0, ""
        );

        showerResponse2 = new ShowerResponse(
            2L, "S02", true, false, "John Doe", startTime, endTime, null, 15.0, "Occupied"
        );

        showerRequest = new ShowerRequest(
            "S01", "Jane Doe", startTime, endTime
        );
    }

    @Test
    void getAllShowers_ShouldReturnAllShowers() throws Exception {
        List<ShowerResponse> responses = Arrays.asList(showerResponse1, showerResponse2);
        when(showerService.getAllShowers()).thenReturn(responses);

        mockMvc.perform(get("/api/showers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].showerNumber", is("S01")))
                .andExpect(jsonPath("$[0].occupied", is(false)))
                .andExpect(jsonPath("$[1].showerNumber", is("S02")))
                .andExpect(jsonPath("$[1].occupied", is(true)))
                .andExpect(jsonPath("$[1].customerName", is("John Doe")));
    }

    @Test
    void getAvailableShowers_ShouldReturnOnlyAvailableShowers() throws Exception {
        List<ShowerResponse> responses = Arrays.asList(showerResponse1);
        when(showerService.getAvailableShowers()).thenReturn(responses);

        mockMvc.perform(get("/api/showers/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].showerNumber", is("S01")))
                .andExpect(jsonPath("$[0].occupied", is(false)));
    }

    @Test
    void getShowerByNumber_WhenShowerExists_ShouldReturnShower() throws Exception {
        when(showerService.getShowerByNumber("S01")).thenReturn(showerResponse1);

        mockMvc.perform(get("/api/showers/S01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.showerNumber", is("S01")))
                .andExpect(jsonPath("$.occupied", is(false)));
    }

    @Test
    void getShowerByNumber_WhenShowerDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(showerService.getShowerByNumber("S99")).thenThrow(new ShowerException("Shower not found"));

        mockMvc.perform(get("/api/showers/S99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createShowerReservation_WhenShowerIsAvailable_ShouldCreateReservation() throws Exception {
        ShowerResponse response = new ShowerResponse(
            1L, "S01", true, false, "Jane Doe", startTime, endTime, null, 15.0, 
            "Shower reservation created successfully"
        );

        when(showerService.createShowerReservation(any(ShowerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/showers/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.showerNumber", is("S01")))
                .andExpect(jsonPath("$.occupied", is(true)))
                .andExpect(jsonPath("$.customerName", is("Jane Doe")))
                .andExpect(jsonPath("$.totalCost", is(15.0)))
                .andExpect(jsonPath("$.message", is("Shower reservation created successfully")));
    }

    @Test
    void createShowerReservation_WhenRateNotFound_ShouldReturnBadRequest() throws Exception {
        when(showerService.createShowerReservation(any(ShowerRequest.class)))
                .thenThrow(new ShowerException("Shower rate not found"));

        mockMvc.perform(post("/api/showers/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void releaseShower_WhenShowerIsOccupied_ShouldReleaseShower() throws Exception {
        ShowerResponse response = new ShowerResponse(
            2L, "S02", false, true, "John Doe", startTime, endTime, 
            LocalDateTime.now().plusMinutes(10).format(formatter), 0.0, 
            "Shower released, cleaners notified"
        );

        when(showerService.releaseShower("S02")).thenReturn(response);

        mockMvc.perform(put("/api/showers/release/S02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.showerNumber", is("S02")))
                .andExpect(jsonPath("$.occupied", is(false)))
                .andExpect(jsonPath("$.cleaning", is(true)))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.message", containsString("Shower released, cleaners notified")));
    }

    @Test
    void releaseShower_WhenShowerDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(showerService.releaseShower("S99")).thenThrow(new ShowerException("Shower not found"));

        mockMvc.perform(put("/api/showers/release/S99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void markShowerClean_WhenShowerIsCleaning_ShouldMarkClean() throws Exception {
        ShowerResponse response = new ShowerResponse(
            3L, "S03", false, false, null, null, null, null, 0.0, 
            "Shower cleaning completed and is now available"
        );

        when(showerService.markShowerClean("S03")).thenReturn(response);

        mockMvc.perform(put("/api/showers/clean/S03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.showerNumber", is("S03")))
                .andExpect(jsonPath("$.occupied", is(false)))
                .andExpect(jsonPath("$.cleaning", is(false)))
                .andExpect(jsonPath("$.message", is("Shower cleaning completed and is now available")));
    }

    @Test
    void markShowerClean_WhenShowerDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(showerService.markShowerClean("S99")).thenThrow(new ShowerException("Shower not found"));

        mockMvc.perform(put("/api/showers/clean/S99"))
                .andExpect(status().isNotFound());
    }
}

package com.truckstopservices.inventory.fuel.controller;

import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.service.FuelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FuelControllerTest {

    @Mock
    private FuelService fuelService;

    private FuelController fuelController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        fuelController = new FuelController(fuelService);
    }

    @Test
    public void testViewInventoryChartData() {
        // Arrange
        List<FuelChartDataResponse> mockChartData = Arrays.asList(
            new FuelChartDataResponse(0, "Total Gallons", "Diesel", 12000),
            new FuelChartDataResponse(1, "Total Gallons", "87", 14000),
            new FuelChartDataResponse(2, "Total Gallons", "93", 5000)
        );
        
        when(fuelService.getFuelInventoryChartData()).thenReturn(mockChartData);
        
        // Act
        ResponseEntity<List<FuelChartDataResponse>> response = fuelController.viewInventoryChartData();
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        List<FuelChartDataResponse> chartData = response.getBody();
        assertEquals(3, chartData.size());
        
        // Check first item
        assertEquals(0, chartData.get(0).id());
        assertEquals("Total Gallons", chartData.get(0).series());
        assertEquals("Diesel", chartData.get(0).group());
        assertEquals(12000, chartData.get(0).value());
        
        // Check second item
        assertEquals(1, chartData.get(1).id());
        assertEquals("Total Gallons", chartData.get(1).series());
        assertEquals("87", chartData.get(1).group());
        assertEquals(14000, chartData.get(1).value());
        
        // Check third item
        assertEquals(2, chartData.get(2).id());
        assertEquals("Total Gallons", chartData.get(2).series());
        assertEquals("93", chartData.get(2).group());
        assertEquals(5000, chartData.get(2).value());
        
        // Debug output
        System.out.println("[DEBUG_LOG] Chart data: " + chartData);
    }
}
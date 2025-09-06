package com.truckstopservices.inventory.fuel.service;

import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import com.truckstopservices.inventory.fuel.repository.DieselRepository;
import com.truckstopservices.inventory.fuel.repository.MidGradeFuelRepository;
import com.truckstopservices.inventory.fuel.repository.PremimumFuelRepository;
import com.truckstopservices.inventory.fuel.repository.RegularFuelRepository;
import com.truckstopservices.accounting.invoice.service.implementation.InvoiceServiceImpl;
import com.truckstopservices.accounting.pos.service.POSService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FuelServiceTest {

    @Mock
    private DieselRepository dieselRepository;

    @Mock
    private RegularFuelRepository regularFuelRepository;

    @Mock
    private MidGradeFuelRepository midGradeFuelRepository;

    @Mock
    private PremimumFuelRepository premimumFuelRepository;

    @Mock
    private InvoiceServiceImpl invoiceServiceImpl;

    @Mock
    private POSService posService;

    private FuelService fuelService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        fuelService = new FuelService(dieselRepository, regularFuelRepository,
                midGradeFuelRepository, premimumFuelRepository,
                invoiceServiceImpl, posService);
    }
    
    @Test
    public void testGetFuelInventoryChartData() {
        // Arrange
        Diesel diesel1 = new Diesel();
        diesel1.setAvailableGallons(10000);
        Diesel diesel2 = new Diesel();
        diesel2.setAvailableGallons(2000);
        
        RegularOctane regular1 = new RegularOctane();
        regular1.setAvailableGallons(5000);
        RegularOctane regular2 = new RegularOctane();
        regular2.setAvailableGallons(9000);
        
        when(dieselRepository.findAll()).thenReturn(Arrays.asList(diesel1, diesel2));
        when(regularFuelRepository.findAll()).thenReturn(Arrays.asList(regular1, regular2));
        when(midGradeFuelRepository.findAll()).thenReturn(List.of());
        when(premimumFuelRepository.findAll()).thenReturn(List.of());
        
        // Act
        List<FuelChartDataResponse> result = fuelService.getFuelInventoryChartData();
        
        // Assert
        assertEquals(2, result.size());
        
        // Verify the first chart data item (Diesel)
        FuelChartDataResponse dieselData = result.stream()
                .filter(data -> "Diesel".equals(data.group()))
                .findFirst()
                .orElseThrow();
        assertEquals(0, dieselData.id());
        assertEquals("Total Gallons", dieselData.series());
        assertEquals(12000.0, dieselData.value());
        
        // Verify the second chart data item (Regular 87)
        FuelChartDataResponse regularData = result.stream()
                .filter(data -> "87".equals(data.group()))
                .findFirst()
                .orElseThrow();
        assertEquals(1, regularData.id());
        assertEquals("Total Gallons", regularData.series());
        assertEquals(14000.0, regularData.value());
        
        // Debug output
        System.out.println("[DEBUG_LOG] Aggregated fuel inventory chart data: " + result);
    }
}

package com.truckstopservices.inventory.fuel.service;

import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import com.truckstopservices.inventory.fuel.repository.DieselRepository;
import com.truckstopservices.inventory.fuel.repository.MidGradeFuelRepository;
import com.truckstopservices.inventory.fuel.repository.PremimumFuelRepository;
import com.truckstopservices.inventory.fuel.repository.RegularFuelRepository;
import com.truckstopservices.accounting.accountsPayable.service.implementation.AccountsPayableImplementation;
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
    private AccountsPayableImplementation accountsPayableImplementation;

    @Mock
    private POSService posService;

    private FuelService fuelService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        fuelService = new FuelService(dieselRepository, regularFuelRepository, 
                                     midGradeFuelRepository, premimumFuelRepository,
                                     accountsPayableImplementation, posService);
    }

    @Test
    public void testGetAllFuelInventory_ShouldAggregateByFuelName() {
        // Arrange
        Diesel diesel1 = new Diesel();
        diesel1.setTotalGallons(10000);
        Diesel diesel2 = new Diesel();
        diesel2.setTotalGallons(2000);
        
        RegularOctane regular1 = new RegularOctane();
        regular1.setTotalGallons(5000);
        RegularOctane regular2 = new RegularOctane();
        regular2.setTotalGallons(9000);
        
        when(dieselRepository.findAll()).thenReturn(Arrays.asList(diesel1, diesel2));
        when(regularFuelRepository.findAll()).thenReturn(Arrays.asList(regular1, regular2));
        when(midGradeFuelRepository.findAll()).thenReturn(List.of());
        when(premimumFuelRepository.findAll()).thenReturn(List.of());
        
        // Act
        List<FuelInventoryResponse> result = fuelService.getAllFuelInventory();
        
        // Convert result to map for easier assertion
        Map<String, Double> resultMap = result.stream()
                .collect(Collectors.toMap(
                        FuelInventoryResponse::fuelName,
                        FuelInventoryResponse::totalGallons
                ));
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(12000.0, resultMap.get("Diesel"));
        assertEquals(14000.0, resultMap.get("87"));
        
        // Debug output
        System.out.println("[DEBUG_LOG] Aggregated fuel inventory: " + result);
    }
}
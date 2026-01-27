package com.truckstopservices.inventory.fuel.controller;

import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleRequest;
import com.truckstopservices.inventory.fuel.dto.FuelSaleResponse;
import com.truckstopservices.inventory.fuel.service.FuelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FuelControllerTest {

    @LocalServerPort
    int port;

    @MockitoBean
    FuelService fuelService;

    private RestClient client;

    @BeforeEach
    void setUp() {
        // FuelController is mapped at "/fuel/**" (no "/api/inventory" prefix from the controller itself)
        this.client = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void viewInventory_returns200() {
        prepopulateInventory();

        var response = client.get()
                .uri("/fuel/viewInventory")
                .retrieve()
                .toEntity(String.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Diesel"));
        assertTrue(response.getBody().contains("5000.0"));
    }

    @Test
    void viewInventoryChartData_returns200() {
        prepopulateInventory();

        var response = client.get()
                .uri("/fuel/viewInventoryChartData")
                .retrieve()
                .toEntity(String.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Diesel"));
        assertTrue(response.getBody().contains("5000.0"));
    }

    @Test
    void reduceGallons_returnsFuelUpdated() {
        doNothing().when(fuelService).updateFuelInventoryDeductAvailableGallonsFromSales(any(Double[].class));

        var response = client.post()
                .uri("/fuel/update/FuelInventory/reduceGallons")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Double[]{1.0, 2.0, 3.0})
                .retrieve()
                .toEntity(String.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Fuel Updated", response.getBody());
    }

    @Test
    void updateDieselFifo_returns200AndResponse() {
        FuelSaleResponse mockResponse = new FuelSaleResponse(0, 100.0, 5.0, "OK", "T1", null);
        when(fuelService.updateDieselInventoryFIFOSales(anyDouble(), anyString())).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/Diesel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(0, 100.0, 5.0, "OK", "T1"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100.0, response.getBody().gallonsSold());
    }

    @Test
    void updateRegularFuelFIFO_returns200AndResponse() {
        FuelSaleResponse mockResponse = new FuelSaleResponse(87, 10.0, 30.0, "Regular", "T2", null);
        when(fuelService.updateRegularOctaneInventoryFIFOSales(anyDouble(), anyString())).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/RegularFuel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(87, 10.0, 30.0, "Regular", "T2"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(87, response.getBody().octane());
    }

    @Test
    void updatePremiumFuelFIFO_returns200AndResponse() {
        FuelSaleResponse mockResponse = new FuelSaleResponse(93, 5.0, 25.0, "Premium", "T3", null);
        when(fuelService.updatePremiumOctaneInventoryFIFOSales(anyDouble(), anyString())).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/PremiumFuel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(93, 5.0, 25.0, "Premium", "T3"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(93, response.getBody().octane());
    }

    @Test
    void updateDieselFifo_batchSwitch_returns200AndResponse() {
        FuelSaleResponse mockResponse = new FuelSaleResponse(0, 20.0, 39.8, "Diesel Fuel Updated. New Batch of Fuel being used. Delivery ID: D2", "T1", null);
        when(fuelService.updateDieselInventoryFIFOSales(20.0, "T1")).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/Diesel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(0, 20.0, 39.8, "OK", "T1"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().specialMessage().contains("New Batch of Fuel being used"));
        assertEquals(20.0, response.getBody().gallonsSold());
    }

    @Test
    void updateRegularFuelFIFO_batchSwitch_returns200AndResponse() {
        FuelSaleResponse mockResponse = new FuelSaleResponse(87, 20.0, 39.8, "Regular Fuel Updated. New Batch of Fuel being used. Delivery ID: R2", "T2", null);
        when(fuelService.updateRegularOctaneInventoryFIFOSales(20.0, "T2")).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/RegularFuel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(87, 20.0, 39.8, "Regular", "T2"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().specialMessage().contains("New Batch of Fuel being used"));
        assertEquals(87, response.getBody().octane());
    }

    @Test
    void updatePremiumFuelFIFO_batchSwitch_returns200AndResponse() {
        FuelSaleResponse mockResponse = new FuelSaleResponse(93, 20.0, 39.8, "Premium Fuel Updated. New Batch of Fuel being used. Delivery ID: P2", "T3", null);
        when(fuelService.updatePremiumOctaneInventoryFIFOSales(20.0, "T3")).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/PremiumFuel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(93, 20.0, 39.8, "Premium", "T3"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().specialMessage().contains("New Batch of Fuel being used"));
        assertEquals(93, response.getBody().octane());
    }

    @Test
    void updateDieselFifo_exactBatchSwitch_returns200() {
        // Request 30 gallons. 20 from Batch A, 10 from Batch B.
        FuelSaleResponse mockResponse = new FuelSaleResponse(0, 30.0, 59.7, "Diesel Fuel Updated. New Batch of Fuel being used. Delivery ID: D-BATCH-B", "T1", null);
        when(fuelService.updateDieselInventoryFIFOSales(30.0, "T1")).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/Diesel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(0, 30.0, 59.7, "OK", "T1"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().specialMessage().contains("New Batch of Fuel being used"));
        assertEquals(30.0, response.getBody().gallonsSold());
    }

    @Test
    void updateRegularFuelFIFO_exactBatchSwitch_returns200() {
        // Request 30 gallons. 20 from Batch A, 10 from Batch B.
        FuelSaleResponse mockResponse = new FuelSaleResponse(87, 30.0, 59.7, "Regular Fuel Updated. New Batch of Fuel being used. Delivery ID: R-BATCH-B", "T2", null);
        when(fuelService.updateRegularOctaneInventoryFIFOSales(30.0, "T2")).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/RegularFuel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(87, 30.0, 59.7, "Regular", "T2"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().specialMessage().contains("New Batch of Fuel being used"));
        assertEquals(87, response.getBody().octane());
        assertEquals(30.0, response.getBody().gallonsSold());
    }

    @Test
    void updatePremiumFuelFIFO_exactBatchSwitch_returns200() {
        // Request 30 gallons. 20 from Batch A, 10 from Batch B.
        FuelSaleResponse mockResponse = new FuelSaleResponse(93, 30.0, 59.7, "Premium Fuel Updated. New Batch of Fuel being used. Delivery ID: P-BATCH-B", "T3", null);
        when(fuelService.updatePremiumOctaneInventoryFIFOSales(30.0, "T3")).thenReturn(mockResponse);

        var response = client.put()
                .uri("/fuel/update/PremiumFuel/FIFO")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FuelSaleRequest(93, 30.0, 59.7, "Premium", "T3"))
                .retrieve()
                .toEntity(FuelSaleResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().specialMessage().contains("New Batch of Fuel being used"));
        assertEquals(93, response.getBody().octane());
        assertEquals(30.0, response.getBody().gallonsSold());
    }

    @Test
    void fuelDeliveryUpdateRepo_returns200() throws Exception {
        when(fuelService.updateFuelDeliveryRepo(any())).thenReturn(new FuelDeliveryResponse(true, "Success", null, null));

        var response = client.put()
                .uri("/fuel/update/FuelInventory/FuelDelivery")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                      {
                        "companyName": "FuelCo",
                        "fuelDeliveryId": "D123",
                        "deliveryDate": "2023-10-27",
                        "dieselOrder": {"octane": 0, "pricePerGallon": 3.5, "totalGallons": 1000.0},
                        "regularOctaneOrder": {"octane": 87, "pricePerGallon": 3.0, "totalGallons": 2000.0},
                        "premiumOctaneOrder": {"octane": 93, "pricePerGallon": 4.0, "totalGallons": 500.0}
                      }
                      """)
                .retrieve()
                .toEntity(FuelDeliveryResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().message());
    }

    @Test
    void fuelDeliveryUpdateRepo_OilSolutions_returns200() {
        prepopulateInventory();
    }

    @Test
    void reduceGallons_throwsException_returns500() {
        doThrow(new RuntimeException("Service Error"))
                .when(fuelService).updateFuelInventoryDeductAvailableGallonsFromSales(any(Double[].class));

        var response = client.post()
                .uri("/fuel/update/FuelInventory/reduceGallons")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Double[]{1.0, 2.0, 3.0})
                .exchange((request, clientResponse) -> {
                    return clientResponse.getStatusCode();
                });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }
    private void prepopulateInventory() {
        try {
            when(fuelService.updateFuelDeliveryRepo(any())).thenReturn(new FuelDeliveryResponse(true, "Success", null, null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<FuelInventoryResponse> mockInventory = List.of(
                new FuelInventoryResponse("Diesel", 5000.0),
                new FuelInventoryResponse("Regular", 100000.0),
                new FuelInventoryResponse("Premium", 70000.0)
        );
        when(fuelService.getAllFuelInventory()).thenReturn(mockInventory);

        List<FuelChartDataResponse> mockChartData = List.of(
                new FuelChartDataResponse(1, "Diesel", "Inventory", 5000.0)
        );
        when(fuelService.getFuelInventoryChartData()).thenReturn(mockChartData);

        client.put()
                .uri("/fuel/update/FuelInventory/FuelDelivery")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                      {
                        "companyName": "Oil Solutions",
                        "fuelDeliveryId": "TD-20241027-001",
                        "deliveryDate": "02/17/2025",
                        "dieselOrder": {
                          "octane": 40,
                          "costPerGallon": 3.75,
                          "totalGallons": 5000.00
                        },
                        "regularOctaneOrder": {
                          "octane": 87,
                          "costPerGallon": 3.25,
                          "totalGallons": 100000.0
                        },
                        "premiumOctaneOrder": {
                          "octane": 91,
                          "costPerGallon": 4.10,
                          "totalGallons": 70000.0
                        }
                      }
                      """)
                .retrieve()
                .toBodilessEntity();
    }
}

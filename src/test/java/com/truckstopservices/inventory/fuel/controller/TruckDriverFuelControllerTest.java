package com.truckstopservices.inventory.fuel.controller;

import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.accounting.model.InvoiceDetail;
import com.truckstopservices.accounting.pos.dto.Receipt;
import com.truckstopservices.accounting.pos.enums.SalesType;
import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleRequest;
import com.truckstopservices.inventory.fuel.dto.FuelSaleResponse;
import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import com.truckstopservices.inventory.fuel.exception.FuelSaleException;
import com.truckstopservices.inventory.fuel.service.TruckDriverFuelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

public class TruckDriverFuelControllerTest {

    @Mock
    private TruckDriverFuelService truckDriverFuelService;

    private TruckDriverFuelController truckDriverFuelController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        truckDriverFuelController = new TruckDriverFuelController(truckDriverFuelService);
    }
    
    @Test
    public void testUpdateDieselFuelFIFO_Success() {
        // Arrange
        double gallonsSold = 100.0;
        FuelSaleRequest request = new FuelSaleRequest(0, gallonsSold, 0, "");
        
        Receipt mockReceipt = new Receipt(
            "R12345",
            LocalDateTime.now(),
            199.0, // 100 gallons * 1.99 per gallon
            SalesType.FUEL
        );
        
        FuelSaleResponse mockResponse = new FuelSaleResponse(
            0,
            gallonsSold,
            199.0,
            "Truck Driver Diesel Fuel Updated",
            mockReceipt
        );
        
        when(truckDriverFuelService.updateDieselInventoryFIFOSales(gallonsSold)).thenReturn(mockResponse);
        
        // Act
        ResponseEntity<FuelSaleResponse> response = truckDriverFuelController.updateDieselFuelFIFO(request);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(gallonsSold, response.getBody().gallonsSold());
        assertEquals(199.0, response.getBody().totalPrice());
        assertEquals("Truck Driver Diesel Fuel Updated", response.getBody().specialMessage());
        assertNotNull(response.getBody().receipt());
        assertEquals("R12345", response.getBody().receipt().receiptId());
    }
    
    @Test
    public void testUpdateDieselFuelFIFO_NoAvailableDiesel() {
        // Arrange
        double gallonsSold = 500.0;
        FuelSaleRequest request = new FuelSaleRequest(0, gallonsSold, 0, "");
        
        String errorMessage = "No available diesel to consume. There are " + gallonsSold + " gallons unaccounted for";
        when(truckDriverFuelService.updateDieselInventoryFIFOSales(gallonsSold))
            .thenThrow(new FuelSaleException(errorMessage));
        
        // Act
        ResponseEntity<FuelSaleResponse> response = truckDriverFuelController.updateDieselFuelFIFO(request);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().specialMessage());
        assertEquals(0, response.getBody().gallonsSold());
        assertEquals(0, response.getBody().totalPrice());
    }
    
    @Test
    public void testUpdateDieselFuelFIFO_ZeroGallons() {
        // Arrange
        double gallonsSold = 0.0;
        FuelSaleRequest request = new FuelSaleRequest(0, gallonsSold, 0, "");
        
        Receipt mockReceipt = new Receipt(
            "R12346",
            LocalDateTime.now(),
            0.0,
            SalesType.FUEL
        );
        
        FuelSaleResponse mockResponse = new FuelSaleResponse(
            0,
            gallonsSold,
            0.0,
            "No diesel fuel was sold, inventory unchanged.",
            mockReceipt
        );
        
        when(truckDriverFuelService.updateDieselInventoryFIFOSales(gallonsSold)).thenReturn(mockResponse);
        
        // Act
        ResponseEntity<FuelSaleResponse> response = truckDriverFuelController.updateDieselFuelFIFO(request);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0.0, response.getBody().gallonsSold());
        assertEquals(0.0, response.getBody().totalPrice());
        assertEquals("No diesel fuel was sold, inventory unchanged.", response.getBody().specialMessage());
    }
    
    @Test
    public void testUpdateDieselFuelFIFO_BatchTransition() {
        // Arrange
        double gallonsSold = 200.0;
        FuelSaleRequest request = new FuelSaleRequest(0, gallonsSold, 0, "");
        
        Receipt mockReceipt = new Receipt(
            "R12347",
            LocalDateTime.now(),
            398.0, // 200 gallons * 1.99 per gallon
            SalesType.FUEL
        );
        
        FuelSaleResponse mockResponse = new FuelSaleResponse(
            0,
            gallonsSold,
            398.0,
            "Truck Driver Diesel Fuel Updated. New Batch of Fuel being used. Delivery ID: 2",
            mockReceipt
        );
        
        when(truckDriverFuelService.updateDieselInventoryFIFOSales(gallonsSold)).thenReturn(mockResponse);
        
        // Act
        ResponseEntity<FuelSaleResponse> response = truckDriverFuelController.updateDieselFuelFIFO(request);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(gallonsSold, response.getBody().gallonsSold());
        assertEquals(398.0, response.getBody().totalPrice());
        assertEquals("Truck Driver Diesel Fuel Updated. New Batch of Fuel being used. Delivery ID: 2", 
                     response.getBody().specialMessage());
    }
    
    @Test
    public void testViewInventoryChartData() {
        // Arrange
        List<FuelChartDataResponse> mockChartData = Arrays.asList(
            new FuelChartDataResponse(1, "Diesel", "Truck Driver", 15000)
        );
        
        when(truckDriverFuelService.getDieselInventoryChartData()).thenReturn(mockChartData);
        
        // Act
        ResponseEntity<List<FuelChartDataResponse>> response = truckDriverFuelController.viewInventoryChartData();
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        List<FuelChartDataResponse> chartData = response.getBody();
        assertNotNull(chartData);
        assertEquals(1, chartData.size());
        
        // Check the chart data
        assertEquals(1, chartData.get(0).id());
        assertEquals("Diesel", chartData.get(0).series());
        assertEquals("Truck Driver", chartData.get(0).group());
        assertEquals(15000, chartData.get(0).value());
    }
    
    @Test
    public void testFuelDeliveryUpdateRepo_Success() throws Exception {
        // Arrange
        Diesel diesel = new Diesel("2025-08-05", 0, 1.75, 5000);
        diesel.setAvailableGallons(5000);
        
        FuelDelivery fuelDelivery = new FuelDelivery();
        fuelDelivery.setCompanyName("Test Fuel Company");
        fuelDelivery.setFuelDelivery_ID("FD12345");
        fuelDelivery.setDeliveryDate("2025-08-05");
        fuelDelivery.setDieselOrder(diesel);
        
        InvoiceDetail invoiceDetail = new InvoiceDetail("Test Fuel Company", "2025-08-05", 8750.0); // 5000 * 1.75
        Invoice invoice = new Invoice("INV12345", invoiceDetail);
        
        FuelDeliveryResponse<FuelDelivery> mockResponse = new FuelDeliveryResponse<>(
            true, 
            "Diesel Successfully Delivered!", 
            new FuelDelivery[0], 
            invoice
        );
        
        when(truckDriverFuelService.updateDieselDeliveryRepo(any(FuelDelivery.class))).thenReturn(mockResponse);
        
        // Act
        ResponseEntity<FuelDeliveryResponse<FuelDelivery>> response = 
            truckDriverFuelController.fuelDeliveryUpdateRepo(fuelDelivery);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        assertEquals("Diesel Successfully Delivered!", response.getBody().message());
        assertNotNull(response.getBody().vendorInvoice());
        assertEquals("INV12345", response.getBody().vendorInvoice().invoiceNumber());
    }
    
    @Test
    public void testFuelDeliveryUpdateRepo_NoDieselOrder() throws Exception {
        // Arrange
        FuelDelivery fuelDelivery = new FuelDelivery();
        fuelDelivery.setCompanyName("Test Fuel Company");
        fuelDelivery.setFuelDelivery_ID("FD12345");
        fuelDelivery.setDeliveryDate("2025-08-05");
        // No diesel order set
        
        String errorMessage = "Diesel order is required for truck driver fuel delivery";
        when(truckDriverFuelService.updateDieselDeliveryRepo(any(FuelDelivery.class)))
            .thenThrow(new Exception(errorMessage));
        
        // Act
        ResponseEntity<FuelDeliveryResponse<FuelDelivery>> response = 
            truckDriverFuelController.fuelDeliveryUpdateRepo(fuelDelivery);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(errorMessage, response.getBody().message());
        assertNull(response.getBody().vendorInvoice());
    }
    
    @Test
    public void testFuelDeliveryUpdateRepo_DataAccessException() throws Exception {
        // Arrange
        Diesel diesel = new Diesel("2025-08-05", 0, 1.75, 5000);
        diesel.setAvailableGallons(5000);
        
        FuelDelivery fuelDelivery = new FuelDelivery();
        fuelDelivery.setCompanyName("Test Fuel Company");
        fuelDelivery.setFuelDelivery_ID("FD12345");
        fuelDelivery.setDeliveryDate("2025-08-05");
        fuelDelivery.setDieselOrder(diesel);
        
        String errorMessage = "Database connection failed";
        when(truckDriverFuelService.updateDieselDeliveryRepo(any(FuelDelivery.class)))
            .thenThrow(new DataAccessResourceFailureException(errorMessage));
        
        // Act & Assert
        assertThrows(DataAccessResourceFailureException.class, () -> {
            truckDriverFuelController.fuelDeliveryUpdateRepo(fuelDelivery);
        });
    }
}
package com.truckstopservices.inventory.fuel.service;

import com.truckstopservices.accounting.invoice.service.implementation.InvoiceServiceImpl;
import com.truckstopservices.accounting.houseaccount.service.HouseAccountTransactionService;
import com.truckstopservices.accounting.invoice.service.implementation.InvoiceServiceImpl;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.accounting.model.InvoiceDetail;
import com.truckstopservices.accounting.pos.dto.Receipt;
import com.truckstopservices.accounting.pos.enums.SalesType;
import com.truckstopservices.accounting.pos.service.POSService;
import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleResponse;
import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import com.truckstopservices.inventory.fuel.exception.FuelSaleException;
import com.truckstopservices.inventory.fuel.repository.DieselRepository;
import com.truckstopservices.inventory.fuel.repository.FuelDeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessResourceFailureException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TruckDriverFuelServiceTest {

    @Mock
    private DieselRepository dieselRepository;

    @Mock
    private FuelDeliveryRepository fuelDeliveryRepository;

    @Mock
    private InvoiceServiceImpl invoiceServiceImpl;

    @Mock
    private POSService posService;
    
    @Mock
    private HouseAccountTransactionService houseAccountTransactionService;

    private TruckDriverFuelService truckDriverFuelService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        truckDriverFuelService = new TruckDriverFuelService(
                dieselRepository,
                fuelDeliveryRepository,
                invoiceServiceImpl,
                posService,
                houseAccountTransactionService
        );
    }

    @Test
    public void testGetDieselInventoryChartData() {
        // Arrange
        Diesel diesel1 = new Diesel("2025-08-01", 0, 1.75, 5000);
        diesel1.setAvailableGallons(5000);
        
        Diesel diesel2 = new Diesel("2025-08-03", 0, 1.80, 3000);
        diesel2.setAvailableGallons(3000);
        
        List<Diesel> dieselList = Arrays.asList(diesel1, diesel2);
        
        when(dieselRepository.findAll()).thenReturn(dieselList);
        
        // Act
        List<FuelChartDataResponse> result = truckDriverFuelService.getDieselInventoryChartData();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).id());
        assertEquals("Diesel", result.get(0).series());
        assertEquals("Truck Driver", result.get(0).group());
        assertEquals(8000, result.get(0).value()); // 5000 + 3000
        
        verify(dieselRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateDieselDeliveryRepo_NullDieselOrder() {
        // Arrange
        FuelDelivery fuelDelivery = new FuelDelivery();
        fuelDelivery.setCompanyName("Fuel Supplier Inc.");
        fuelDelivery.setDeliveryDate("2025-08-05");
        fuelDelivery.setDieselOrder(null); // Null diesel order
        
        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            truckDriverFuelService.updateDieselDeliveryRepo(fuelDelivery);
        });
        
        assertEquals("Diesel order is required for truck driver fuel delivery", exception.getMessage());
        
        verify(fuelDeliveryRepository, never()).save(any(FuelDelivery.class));
    }

    @Test
    public void testUpdateDieselInventoryFIFOSales_Success() {
        // Arrange
        double gallonsSold = 100.0;
        
        Diesel diesel = new Diesel("2025-08-01", 0, 1.85, 5000);
        diesel.setAvailableGallons(5000);
        
        Receipt mockReceipt = new Receipt(
            "R12345",
            LocalDateTime.now(),
            199.0, // 100 gallons * 1.99 per gallon
            SalesType.FUEL
        );
        
        when(dieselRepository.findFIFOAvailableGallons()).thenReturn(Optional.of(diesel));
        when(dieselRepository.save(any(Diesel.class))).thenReturn(diesel);
        when(posService.createPOSRecord(anyDouble(), any(SalesType.class))).thenReturn(mockReceipt);
        
        // Act
        FuelSaleResponse result = truckDriverFuelService.updateDieselInventoryFIFOSales(gallonsSold);
        
        // Assert
        assertNotNull(result);
        assertEquals(gallonsSold, result.gallonsSold());
        assertEquals(199.0, result.totalPrice());
        assertEquals("Truck Driver Diesel Fuel Updated", result.specialMessage());
        assertNotNull(result.receipt());
        assertEquals("R12345", result.receipt().receiptId());
        
        verify(dieselRepository, times(1)).findFIFOAvailableGallons();
        verify(dieselRepository, times(1)).save(any(Diesel.class));
        verify(posService, times(1)).createPOSRecord(anyDouble(), any(SalesType.class));
    }

    @Test
    public void testUpdateDieselInventoryFIFOSales_NoAvailableDiesel() {
        // Arrange
        double gallonsSold = 100.0;
        
        when(dieselRepository.findFIFOAvailableGallons()).thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(FuelSaleException.class, () -> {
            truckDriverFuelService.updateDieselInventoryFIFOSales(gallonsSold);
        });
        
        assertEquals("No available diesel to consume. There are 100.0 gallons unaccounted for", exception.getMessage());
        
        verify(dieselRepository, times(1)).findFIFOAvailableGallons();
        verify(dieselRepository, never()).save(any(Diesel.class));
        verify(posService, never()).createPOSRecord(anyDouble(), any(SalesType.class));
    }

    @Test
    public void testUpdateDieselInventoryFIFOSales_ExactlyEnoughGallons() {
        // Arrange
        double gallonsSold = 5000.0;
        
        Diesel firstDiesel = new Diesel("2025-08-01", 0, 1.85, 5000);
        firstDiesel.setAvailableGallons(5000);
        
        Diesel secondDiesel = new Diesel("2025-08-03", 0, 1.90, 3000);
        secondDiesel.setAvailableGallons(3000);
        
        Receipt mockReceipt = new Receipt(
            "R12345",
            LocalDateTime.now(),
            9950.0, // 5000 gallons * 1.99 per gallon
            SalesType.FUEL
        );
        
        when(dieselRepository.findFIFOAvailableGallons()).thenReturn(Optional.of(firstDiesel));
        when(dieselRepository.findNextFifoNextAvailableGallons()).thenReturn(Optional.of(secondDiesel));
        when(dieselRepository.save(any(Diesel.class))).thenReturn(firstDiesel);
        when(posService.createPOSRecord(anyDouble(), any(SalesType.class))).thenReturn(mockReceipt);
        
        // Act
        FuelSaleResponse result = truckDriverFuelService.updateDieselInventoryFIFOSales(gallonsSold);
        
        // Assert
        assertNotNull(result);
        assertEquals(gallonsSold, result.gallonsSold());
        assertEquals(9950.0, result.totalPrice());
        assertTrue(result.specialMessage().contains("Truck Driver Diesel Fuel Updated"));
        assertTrue(result.specialMessage().contains("New Batch of Fuel being used"));
        assertNotNull(result.receipt());
        
        verify(dieselRepository, times(1)).findFIFOAvailableGallons();
        verify(dieselRepository, times(1)).findNextFifoNextAvailableGallons();
        verify(dieselRepository, times(1)).save(any(Diesel.class));
        verify(posService, times(1)).createPOSRecord(anyDouble(), any(SalesType.class));
    }

    @Test
    public void testUpdateDieselInventoryFIFOSales_NotEnoughGallonsAndNoNextBatch() {
        // Arrange
        double gallonsSold = 6000.0;
        
        Diesel firstDiesel = new Diesel("2025-08-01", 0, 1.85, 5000);
        firstDiesel.setAvailableGallons(5000);
        
        when(dieselRepository.findFIFOAvailableGallons()).thenReturn(Optional.of(firstDiesel));
        when(dieselRepository.findNextFifoNextAvailableGallons()).thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(FuelSaleException.class, () -> {
            truckDriverFuelService.updateDieselInventoryFIFOSales(gallonsSold);
        });
        
        assertTrue(exception.getMessage().contains("Next batch of diesel fuel is unavailable"));
        assertTrue(exception.getMessage().contains("1000.0 gallons are not accounted for"));
        
        verify(dieselRepository, times(1)).findFIFOAvailableGallons();
        verify(dieselRepository, times(1)).findNextFifoNextAvailableGallons();
        verify(posService, never()).createPOSRecord(anyDouble(), any(SalesType.class));
    }
}
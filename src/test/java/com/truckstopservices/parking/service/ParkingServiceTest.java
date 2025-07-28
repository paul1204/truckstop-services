package com.truckstopservices.parking.service;

import com.truckstopservices.parking.dto.ParkingRequest;
import com.truckstopservices.parking.dto.ParkingResponse;
import com.truckstopservices.parking.entity.ParkingRate;
import com.truckstopservices.parking.entity.ParkingRate.RateType;
import com.truckstopservices.parking.entity.ParkingSpot;
import com.truckstopservices.parking.exception.ParkingException;
import com.truckstopservices.parking.repository.ParkingRateRepository;
import com.truckstopservices.parking.repository.ParkingSpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private ParkingRateRepository parkingRateRepository;

    @InjectMocks
    private ParkingService parkingService;

    private ParkingSpot parkingSpot;
    private ParkingRate hourlyRate;
    private ParkingRate nightlyRate;
    private ParkingRate weeklyRate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private String startTime;
    private String endTime;

    @BeforeEach
    void setUp() {
        // Initialize test data
        parkingSpot = new ParkingSpot("A01", false, null, null, null);
        // ID will be auto-generated

        hourlyRate = new ParkingRate(RateType.HOURLY, 5.99, "Standard hourly rate");
        nightlyRate = new ParkingRate(RateType.NIGHTLY, 29.99, "Overnight parking");
        weeklyRate = new ParkingRate(RateType.WEEKLY, 149.99, "Weekly rate package");

        LocalDateTime now = LocalDateTime.now();
        startTime = now.format(formatter);
        endTime = now.plusHours(3).format(formatter);
    }

    @Test
    void getAllParkingSpots_ShouldReturnAllSpots() {
        // Arrange
        List<ParkingSpot> spots = Arrays.asList(
            new ParkingSpot("A01", false, null, null, null),
            new ParkingSpot("A02", true, startTime, endTime, "ABC123")
        );
        when(parkingSpotRepository.findAll()).thenReturn(spots);

        // Act
        List<ParkingResponse> result = parkingService.getAllParkingSpots();

        // Assert
        assertEquals(2, result.size());
        assertEquals("A01", result.get(0).spotNumber());
        assertEquals("A02", result.get(1).spotNumber());
        assertTrue(result.get(1).occupied());
        assertEquals("ABC123", result.get(1).vehicleRegistration());
        verify(parkingSpotRepository, times(1)).findAll();
    }

    @Test
    void getAvailableParkingSpots_ShouldReturnOnlyAvailableSpots() {
        // Arrange
        List<ParkingSpot> spots = Arrays.asList(
            new ParkingSpot("A01", false, null, null, null),
            new ParkingSpot("A03", false, null, null, null)
        );
        when(parkingSpotRepository.findAvailableSpots()).thenReturn(spots);

        // Act
        List<ParkingResponse> result = parkingService.getAvailableParkingSpots();

        // Assert
        assertEquals(2, result.size());
        assertFalse(result.get(0).occupied());
        assertFalse(result.get(1).occupied());
        verify(parkingSpotRepository, times(1)).findAvailableSpots();
    }

    @Test
    void getParkingSpotByNumber_WhenSpotExists_ShouldReturnSpot() {
        // Arrange
        when(parkingSpotRepository.findBySpotNumber("A01")).thenReturn(Optional.of(parkingSpot));

        // Act
        ParkingResponse result = parkingService.getParkingSpotByNumber("A01");

        // Assert
        assertEquals("A01", result.spotNumber());
        assertFalse(result.occupied());
        verify(parkingSpotRepository, times(1)).findBySpotNumber("A01");
    }

    @Test
    void getParkingSpotByNumber_WhenSpotDoesNotExist_ShouldThrowException() {
        // Arrange
        when(parkingSpotRepository.findBySpotNumber("A99")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ParkingException.class, () -> parkingService.getParkingSpotByNumber("A99"));
        verify(parkingSpotRepository, times(1)).findBySpotNumber("A99");
    }

    @Test
    void createParkingSpotReservation_WhenSpotIsAvailable_ShouldCreateReservation() {
        // Arrange
        ParkingRequest request = new ParkingRequest("A01", "XYZ789", RateType.HOURLY, startTime, endTime);

        when(parkingSpotRepository.findBySpotNumber("A01")).thenReturn(Optional.of(parkingSpot));
        when(parkingRateRepository.findByRateType(RateType.HOURLY)).thenReturn(Optional.of(hourlyRate));
        when(parkingSpotRepository.save(any(ParkingSpot.class))).thenReturn(parkingSpot);

        // Act
        ParkingResponse result = parkingService.createParkingSpotReservation(request);

        // Assert
        assertEquals("A01", result.spotNumber());
        assertTrue(result.occupied());
        assertEquals("XYZ789", result.vehicleRegistration());
        assertEquals(RateType.HOURLY, result.rateType());
        assertEquals(startTime, result.occupiedSince());
        assertEquals(endTime, result.reservedUntil());
        assertTrue(result.totalCost() > 0);
        assertEquals("Parking spot reservation created successfully", result.message());

        verify(parkingSpotRepository, times(1)).findBySpotNumber("A01");
        verify(parkingRateRepository, times(1)).findByRateType(RateType.HOURLY);
        verify(parkingSpotRepository, times(1)).save(any(ParkingSpot.class));
    }

    @Test
    void createParkingSpotReservation_WhenSpotIsOccupied_ShouldReturnOccupiedMessage() {
        // Arrange
        ParkingSpot occupiedSpot = new ParkingSpot("A02", true, startTime, endTime, "ABC123");
        // ID will be auto-generated

        ParkingRequest request = new ParkingRequest("A02", "XYZ789", RateType.HOURLY, startTime, endTime);

        when(parkingSpotRepository.findBySpotNumber("A02")).thenReturn(Optional.of(occupiedSpot));

        // Act
        ParkingResponse result = parkingService.createParkingSpotReservation(request);

        // Assert
        assertEquals("A02", result.spotNumber());
        assertTrue(result.occupied());
        assertEquals("ABC123", result.vehicleRegistration());
        assertEquals("Spot is already occupied", result.message());

        verify(parkingSpotRepository, times(1)).findBySpotNumber("A02");
        verify(parkingRateRepository, never()).findByRateType(any());
        verify(parkingSpotRepository, never()).save(any(ParkingSpot.class));
    }

    @Test
    void createParkingSpotReservation_WhenRateTypeDoesNotExist_ShouldThrowException() {
        // Arrange
        ParkingRequest request = new ParkingRequest("A01", "XYZ789", RateType.HOURLY, startTime, endTime);

        when(parkingSpotRepository.findBySpotNumber("A01")).thenReturn(Optional.of(parkingSpot));
        when(parkingRateRepository.findByRateType(RateType.HOURLY)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ParkingException.class, () -> parkingService.createParkingSpotReservation(request));

        verify(parkingSpotRepository, times(1)).findBySpotNumber("A01");
        verify(parkingRateRepository, times(1)).findByRateType(RateType.HOURLY);
        verify(parkingSpotRepository, never()).save(any(ParkingSpot.class));
    }

    @Test
    void releaseParkingSpot_WhenSpotIsOccupied_ShouldReleaseSpot() {
        // Arrange
        ParkingSpot occupiedSpot = new ParkingSpot("A02", true, startTime, endTime, "ABC123");
        // ID will be auto-generated

        when(parkingSpotRepository.findBySpotNumber("A02")).thenReturn(Optional.of(occupiedSpot));
        when(parkingSpotRepository.save(any(ParkingSpot.class))).thenAnswer(invocation -> {
            ParkingSpot spot = invocation.getArgument(0);
            spot.setOccupied(false);
            spot.setVehicleRegistration(null);
            spot.setOccupiedSince(null);
            spot.setReservedUntil(null);
            return spot;
        });

        // Act
        ParkingResponse result = parkingService.releaseParkingSpot("A02");

        // Assert
        assertEquals("A02", result.spotNumber());
        assertFalse(result.occupied());
        assertEquals("ABC123", result.vehicleRegistration());
        assertEquals(startTime, result.occupiedSince());
        assertEquals(endTime, result.reservedUntil());
        assertEquals("Parking spot released successfully", result.message());

        verify(parkingSpotRepository, times(1)).findBySpotNumber("A02");
        verify(parkingSpotRepository, times(1)).save(any(ParkingSpot.class));
    }

    @Test
    void releaseParkingSpot_WhenSpotIsNotOccupied_ShouldReturnVacantMessage() {
        // Arrange
        when(parkingSpotRepository.findBySpotNumber("A01")).thenReturn(Optional.of(parkingSpot));

        // Act
        ParkingResponse result = parkingService.releaseParkingSpot("A01");

        // Assert
        assertEquals("A01", result.spotNumber());
        assertFalse(result.occupied());
        assertNull(result.vehicleRegistration());
        assertEquals("Spot is already vacant", result.message());

        verify(parkingSpotRepository, times(1)).findBySpotNumber("A01");
        verify(parkingSpotRepository, never()).save(any(ParkingSpot.class));
    }

    @Test
    void releaseParkingSpot_WhenSpotDoesNotExist_ShouldThrowException() {
        // Arrange
        when(parkingSpotRepository.findBySpotNumber("A99")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ParkingException.class, () -> parkingService.releaseParkingSpot("A99"));

        verify(parkingSpotRepository, times(1)).findBySpotNumber("A99");
        verify(parkingSpotRepository, never()).save(any(ParkingSpot.class));
    }
}

package com.truckstopservices.shower.service;

import com.truckstopservices.shower.dto.ShowerRequest;
import com.truckstopservices.shower.dto.ShowerResponse;
import com.truckstopservices.shower.entity.Shower;
import com.truckstopservices.shower.entity.ShowerRate;
import com.truckstopservices.shower.exception.ShowerException;
import com.truckstopservices.shower.repository.ShowerRepository;
import com.truckstopservices.shower.repository.ShowerRateRepository;
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
public class ShowerServiceTest {

    @Mock
    private ShowerRepository showerRepository;

    @Mock
    private ShowerRateRepository showerRateRepository;

    @InjectMocks
    private ShowerService showerService;

    private Shower shower;
    private ShowerRate showerRate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private String startTime;
    private String endTime;

    @BeforeEach
    void setUp() {
        // Initialize test data
        shower = new Shower("S01", false, false, null, null, null, null);
        
        showerRate = new ShowerRate(15.0, "Standard shower rate", 1);
        
        LocalDateTime now = LocalDateTime.now();
        startTime = now.format(formatter);
        endTime = now.plusHours(1).format(formatter);
    }

    @Test
    void getAllShowers_ShouldReturnAllShowers() {
        // Arrange
        List<Shower> showers = Arrays.asList(
            new Shower("S01", false, false, null, null, null, null),
            new Shower("S02", true, false, startTime, endTime, null, "John Doe")
        );
        when(showerRepository.findAll()).thenReturn(showers);

        // Act
        List<ShowerResponse> result = showerService.getAllShowers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("S01", result.get(0).showerNumber());
        assertEquals("S02", result.get(1).showerNumber());
        assertTrue(result.get(1).occupied());
        assertEquals("John Doe", result.get(1).customerName());
        verify(showerRepository, times(1)).findAll();
    }

    @Test
    void getAvailableShowers_ShouldReturnOnlyAvailableShowers() {
        // Arrange
        List<Shower> showers = Arrays.asList(
            new Shower("S01", false, false, null, null, null, null),
            new Shower("S03", false, false, null, null, null, null)
        );
        when(showerRepository.findAvailableShowers()).thenReturn(showers);

        // Act
        List<ShowerResponse> result = showerService.getAvailableShowers();

        // Assert
        assertEquals(2, result.size());
        assertFalse(result.get(0).occupied());
        assertFalse(result.get(1).occupied());
        verify(showerRepository, times(1)).findAvailableShowers();
    }

    @Test
    void getShowerByNumber_WhenShowerExists_ShouldReturnShower() {
        // Arrange
        when(showerRepository.findByShowerNumber("S01")).thenReturn(Optional.of(shower));

        // Act
        ShowerResponse result = showerService.getShowerByNumber("S01");

        // Assert
        assertEquals("S01", result.showerNumber());
        assertFalse(result.occupied());
        verify(showerRepository, times(1)).findByShowerNumber("S01");
    }

    @Test
    void getShowerByNumber_WhenShowerDoesNotExist_ShouldThrowException() {
        // Arrange
        when(showerRepository.findByShowerNumber("S99")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ShowerException.class, () -> showerService.getShowerByNumber("S99"));
        verify(showerRepository, times(1)).findByShowerNumber("S99");
    }

    @Test
    void createShowerReservation_WhenShowerIsAvailable_ShouldCreateReservation() {
        // Arrange
        ShowerRequest request = new ShowerRequest("S01", "John Doe", startTime, endTime);

        when(showerRepository.findByShowerNumber("S01")).thenReturn(Optional.of(shower));
        when(showerRateRepository.findAll()).thenReturn(List.of(showerRate));
        when(showerRepository.save(any(Shower.class))).thenAnswer(invocation -> {
            Shower savedShower = invocation.getArgument(0);
            savedShower.setOccupied(true);
            savedShower.setCustomerName("John Doe");
            savedShower.setOccupiedSince(startTime);
            savedShower.setReservedUntil(endTime);
            return savedShower;
        });

        // Act
        ShowerResponse result = showerService.createShowerReservation(request);

        // Assert
        assertEquals("S01", result.showerNumber());
        assertTrue(result.occupied());
        assertEquals("John Doe", result.customerName());
        assertEquals(startTime, result.occupiedSince());
        assertEquals(endTime, result.reservedUntil());
        assertEquals(15.0, result.totalCost());
        assertEquals("Shower reservation created successfully", result.message());

        verify(showerRepository, times(1)).findByShowerNumber("S01");
        verify(showerRateRepository, times(1)).findAll();
        verify(showerRepository, times(1)).save(any(Shower.class));
    }

    @Test
    void createShowerReservation_WhenShowerIsOccupied_ShouldReturnOccupiedMessage() {
        // Arrange
        Shower occupiedShower = new Shower("S02", true, false, startTime, endTime, null, "Jane Doe");

        ShowerRequest request = new ShowerRequest("S02", "John Doe", startTime, endTime);

        when(showerRepository.findByShowerNumber("S02")).thenReturn(Optional.of(occupiedShower));

        // Act
        ShowerResponse result = showerService.createShowerReservation(request);

        // Assert
        assertEquals("S02", result.showerNumber());
        assertTrue(result.occupied());
        assertEquals("Jane Doe", result.customerName());
        assertEquals("Shower is currently occupied", result.message());

        verify(showerRepository, times(1)).findByShowerNumber("S02");
        verify(showerRateRepository, never()).findAll();
        verify(showerRepository, never()).save(any(Shower.class));
    }

    @Test
    void createShowerReservation_WhenShowerIsCleaning_ShouldReturnCleaningMessage() {
        // Arrange
        Shower cleaningShower = new Shower("S03", false, true, null, null, endTime, null);

        ShowerRequest request = new ShowerRequest("S03", "John Doe", startTime, endTime);

        when(showerRepository.findByShowerNumber("S03")).thenReturn(Optional.of(cleaningShower));

        // Act
        ShowerResponse result = showerService.createShowerReservation(request);

        // Assert
        assertEquals("S03", result.showerNumber());
        assertFalse(result.occupied());
        assertTrue(result.cleaning());
        assertEquals("Shower is currently being cleaned", result.message());

        verify(showerRepository, times(1)).findByShowerNumber("S03");
        verify(showerRateRepository, never()).findAll();
        verify(showerRepository, never()).save(any(Shower.class));
    }

    @Test
    void createShowerReservation_WhenRateNotFound_ShouldThrowException() {
        // Arrange
        ShowerRequest request = new ShowerRequest("S01", "John Doe", startTime, endTime);

        when(showerRepository.findByShowerNumber("S01")).thenReturn(Optional.of(shower));
        when(showerRateRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        assertThrows(ShowerException.class, () -> showerService.createShowerReservation(request));

        verify(showerRepository, times(1)).findByShowerNumber("S01");
        verify(showerRateRepository, times(1)).findAll();
        verify(showerRepository, never()).save(any(Shower.class));
    }

    @Test
    void createShowerReservation_WhenReservationExceedsMaxHours_ShouldThrowException() {
        // Arrange
        String longEndTime = LocalDateTime.parse(startTime, formatter).plusHours(2).format(formatter);
        ShowerRequest request = new ShowerRequest("S01", "John Doe", startTime, longEndTime);

        when(showerRepository.findByShowerNumber("S01")).thenReturn(Optional.of(shower));
        when(showerRateRepository.findAll()).thenReturn(List.of(showerRate));

        // Act & Assert
        assertThrows(ShowerException.class, () -> showerService.createShowerReservation(request));

        verify(showerRepository, times(1)).findByShowerNumber("S01");
        verify(showerRateRepository, times(1)).findAll();
        verify(showerRepository, never()).save(any(Shower.class));
    }

    @Test
    void releaseShower_WhenShowerIsOccupied_ShouldReleaseShower() {
        // Arrange
        Shower occupiedShower = new Shower("S02", true, false, startTime, endTime, null, "Jane Doe");

        when(showerRepository.findByShowerNumber("S02")).thenReturn(Optional.of(occupiedShower));
        when(showerRepository.save(any(Shower.class))).thenAnswer(invocation -> {
            Shower savedShower = invocation.getArgument(0);
            savedShower.setOccupied(false);
            savedShower.setCleaning(true);
            savedShower.setCustomerName(null);
            savedShower.setOccupiedSince(null);
            savedShower.setReservedUntil(null);
            savedShower.setCleaningUntil(LocalDateTime.now().plusMinutes(10).format(formatter));
            return savedShower;
        });

        // Act
        ShowerResponse result = showerService.releaseShower("S02");

        // Assert
        assertEquals("S02", result.showerNumber());
        assertFalse(result.occupied());
        assertTrue(result.cleaning());
        assertEquals("Jane Doe", result.customerName());
        assertEquals(startTime, result.occupiedSince());
        assertEquals(endTime, result.reservedUntil());
        assertNotNull(result.cleaningUntil());
        assertTrue(result.message().contains("Shower released, cleaners notified"));

        verify(showerRepository, times(1)).findByShowerNumber("S02");
        verify(showerRepository, times(1)).save(any(Shower.class));
    }

    @Test
    void releaseShower_WhenShowerIsNotOccupied_ShouldReturnVacantMessage() {
        // Arrange
        when(showerRepository.findByShowerNumber("S01")).thenReturn(Optional.of(shower));

        // Act
        ShowerResponse result = showerService.releaseShower("S01");

        // Assert
        assertEquals("S01", result.showerNumber());
        assertFalse(result.occupied());
        assertNull(result.customerName());
        assertEquals("Shower is already vacant", result.message());

        verify(showerRepository, times(1)).findByShowerNumber("S01");
        verify(showerRepository, never()).save(any(Shower.class));
    }

    @Test
    void markShowerClean_WhenShowerIsCleaning_ShouldMarkClean() {
        // Arrange
        Shower cleaningShower = new Shower("S03", false, true, null, null, endTime, null);

        when(showerRepository.findByShowerNumber("S03")).thenReturn(Optional.of(cleaningShower));
        when(showerRepository.save(any(Shower.class))).thenAnswer(invocation -> {
            Shower savedShower = invocation.getArgument(0);
            savedShower.setCleaning(false);
            savedShower.setCleaningUntil(null);
            return savedShower;
        });

        // Act
        ShowerResponse result = showerService.markShowerClean("S03");

        // Assert
        assertEquals("S03", result.showerNumber());
        assertFalse(result.occupied());
        assertFalse(result.cleaning());
        assertNull(result.cleaningUntil());
        assertEquals("Shower cleaning completed and is now available", result.message());

        verify(showerRepository, times(1)).findByShowerNumber("S03");
        verify(showerRepository, times(1)).save(any(Shower.class));
    }

    @Test
    void markShowerClean_WhenShowerIsNotCleaning_ShouldReturnNotCleaningMessage() {
        // Arrange
        when(showerRepository.findByShowerNumber("S01")).thenReturn(Optional.of(shower));

        // Act
        ShowerResponse result = showerService.markShowerClean("S01");

        // Assert
        assertEquals("S01", result.showerNumber());
        assertFalse(result.occupied());
        assertFalse(result.cleaning());
        assertEquals("Shower is not in cleaning state", result.message());

        verify(showerRepository, times(1)).findByShowerNumber("S01");
        verify(showerRepository, never()).save(any(Shower.class));
    }
}
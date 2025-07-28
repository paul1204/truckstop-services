package com.truckstopservices.parking.repository;

import com.truckstopservices.parking.entity.ParkingRate;
import com.truckstopservices.parking.entity.ParkingRate.RateType;
import com.truckstopservices.parking.entity.ParkingSpot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ParkingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Autowired
    private ParkingRateRepository parkingRateRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Test
    void parkingSpotRepository_FindBySpotNumber_ShouldReturnSpot() {
        // Arrange
        ParkingSpot spot = new ParkingSpot("A01", false, null, null, null);
        entityManager.persist(spot);
        entityManager.flush();

        // Act
        Optional<ParkingSpot> found = parkingSpotRepository.findBySpotNumber("A01");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("A01", found.get().getSpotNumber());
        assertFalse(found.get().isOccupied());
    }

    @Test
    void parkingSpotRepository_FindBySpotNumber_WhenSpotDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<ParkingSpot> found = parkingSpotRepository.findBySpotNumber("A99");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void parkingSpotRepository_FindAvailableSpots_ShouldReturnOnlyAvailableSpots() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String startTime = now.format(formatter);
        String endTime = now.plusHours(3).format(formatter);

        ParkingSpot spot1 = new ParkingSpot("A01", false, null, null, null);
        ParkingSpot spot2 = new ParkingSpot("A02", true, startTime, endTime, "ABC123");
        ParkingSpot spot3 = new ParkingSpot("A03", false, null, null, null);

        entityManager.persist(spot1);
        entityManager.persist(spot2);
        entityManager.persist(spot3);
        entityManager.flush();

        // Act
        List<ParkingSpot> availableSpots = parkingSpotRepository.findAvailableSpots();

        // Assert
        assertEquals(2, availableSpots.size());
        assertTrue(availableSpots.stream().noneMatch(ParkingSpot::isOccupied));
        assertTrue(availableSpots.stream().anyMatch(spot -> spot.getSpotNumber().equals("A01")));
        assertTrue(availableSpots.stream().anyMatch(spot -> spot.getSpotNumber().equals("A03")));
    }

    @Test
    void parkingSpotRepository_FindByVehicleRegistration_ShouldReturnSpot() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String startTime = now.format(formatter);
        String endTime = now.plusHours(3).format(formatter);

        ParkingSpot spot = new ParkingSpot("A02", true, startTime, endTime, "ABC123");
        entityManager.persist(spot);
        entityManager.flush();

        // Act
        Optional<ParkingSpot> found = parkingSpotRepository.findByVehicleRegistration("ABC123");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("A02", found.get().getSpotNumber());
        assertTrue(found.get().isOccupied());
        assertEquals("ABC123", found.get().getVehicleRegistration());
    }

    @Test
    void parkingRateRepository_FindByRateType_ShouldReturnRate() {
        // Arrange
        ParkingRate hourlyRate = new ParkingRate(RateType.HOURLY, 5.99, "Standard hourly rate");
        entityManager.persist(hourlyRate);
        entityManager.flush();

        // Act
        Optional<ParkingRate> found = parkingRateRepository.findByRateType(RateType.HOURLY);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(RateType.HOURLY, found.get().getRateType());
        assertEquals(5.99, found.get().getRate());
        assertEquals("Standard hourly rate", found.get().getDescription());
    }

    @Test
    void parkingRateRepository_FindByRateType_WhenRateDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<ParkingRate> found = parkingRateRepository.findByRateType(RateType.WEEKLY);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void parkingRateRepository_SaveAndFindAll_ShouldSaveAndReturnAllRates() {
        // Arrange
        ParkingRate hourlyRate = new ParkingRate(RateType.HOURLY, 5.99, "Standard hourly rate");
        ParkingRate nightlyRate = new ParkingRate(RateType.NIGHTLY, 29.99, "Overnight parking");
        ParkingRate weeklyRate = new ParkingRate(RateType.WEEKLY, 149.99, "Weekly rate package");

        entityManager.persist(hourlyRate);
        entityManager.persist(nightlyRate);
        entityManager.persist(weeklyRate);
        entityManager.flush();

        // Act
        List<ParkingRate> rates = parkingRateRepository.findAll();

        // Assert
        assertEquals(3, rates.size());
        assertTrue(rates.stream().anyMatch(rate -> rate.getRateType() == RateType.HOURLY));
        assertTrue(rates.stream().anyMatch(rate -> rate.getRateType() == RateType.NIGHTLY));
        assertTrue(rates.stream().anyMatch(rate -> rate.getRateType() == RateType.WEEKLY));
    }
}
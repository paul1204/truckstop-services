package com.truckstopservices.shower.repository;

import com.truckstopservices.shower.entity.Shower;
import com.truckstopservices.shower.entity.ShowerRate;
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
public class ShowerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShowerRepository showerRepository;

    @Autowired
    private ShowerRateRepository showerRateRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Test
    void showerRepository_FindByShowerNumber_ShouldReturnShower() {
        // Arrange
        Shower shower = new Shower("S01", false, false, null, null, null, null);
        entityManager.persist(shower);
        entityManager.flush();

        // Act
        Optional<Shower> found = showerRepository.findByShowerNumber("S01");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("S01", found.get().getShowerNumber());
        assertFalse(found.get().isOccupied());
        assertFalse(found.get().isCleaning());
    }

    @Test
    void showerRepository_FindByShowerNumber_WhenShowerDoesNotExist_ShouldReturnEmpty() {
        // Act
        Optional<Shower> found = showerRepository.findByShowerNumber("S99");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void showerRepository_FindAvailableShowers_ShouldReturnOnlyAvailableShowers() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String startTime = now.format(formatter);
        String endTime = now.plusHours(1).format(formatter);
        String cleaningUntil = now.plusMinutes(10).format(formatter);

        Shower shower1 = new Shower("S01", false, false, null, null, null, null);
        Shower shower2 = new Shower("S02", true, false, startTime, endTime, null, "John Doe");
        Shower shower3 = new Shower("S03", false, true, null, null, cleaningUntil, null);
        Shower shower4 = new Shower("S04", false, false, null, null, null, null);

        entityManager.persist(shower1);
        entityManager.persist(shower2);
        entityManager.persist(shower3);
        entityManager.persist(shower4);
        entityManager.flush();

        // Act
        List<Shower> availableShowers = showerRepository.findAvailableShowers();

        // Assert
        assertEquals(2, availableShowers.size());
        assertTrue(availableShowers.stream().noneMatch(Shower::isOccupied));
        assertTrue(availableShowers.stream().noneMatch(Shower::isCleaning));
        assertTrue(availableShowers.stream().anyMatch(shower -> shower.getShowerNumber().equals("S01")));
        assertTrue(availableShowers.stream().anyMatch(shower -> shower.getShowerNumber().equals("S04")));
    }

    @Test
    void showerRepository_FindByCustomerName_ShouldReturnShower() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String startTime = now.format(formatter);
        String endTime = now.plusHours(1).format(formatter);

        Shower shower = new Shower("S02", true, false, startTime, endTime, null, "John Doe");
        entityManager.persist(shower);
        entityManager.flush();

        // Act
        Optional<Shower> found = showerRepository.findByCustomerName("John Doe");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("S02", found.get().getShowerNumber());
        assertTrue(found.get().isOccupied());
        assertEquals("John Doe", found.get().getCustomerName());
    }

    @Test
    void showerRepository_FindShowersInCleaning_ShouldReturnShowersInCleaning() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String cleaningUntil = now.plusMinutes(10).format(formatter);

        Shower shower1 = new Shower("S01", false, false, null, null, null, null);
        Shower shower2 = new Shower("S02", false, true, null, null, cleaningUntil, null);
        Shower shower3 = new Shower("S03", false, true, null, null, cleaningUntil, null);

        entityManager.persist(shower1);
        entityManager.persist(shower2);
        entityManager.persist(shower3);
        entityManager.flush();

        // Act
        List<Shower> showersInCleaning = showerRepository.findShowersInCleaning();

        // Assert
        assertEquals(2, showersInCleaning.size());
        assertTrue(showersInCleaning.stream().allMatch(Shower::isCleaning));
        assertTrue(showersInCleaning.stream().anyMatch(shower -> shower.getShowerNumber().equals("S02")));
        assertTrue(showersInCleaning.stream().anyMatch(shower -> shower.getShowerNumber().equals("S03")));
    }

    @Test
    void showerRateRepository_SaveAndFindAll_ShouldSaveAndReturnAllRates() {
        // Arrange
        ShowerRate standardRate = new ShowerRate(15.0, "Standard shower rate", 1);
        ShowerRate premiumRate = new ShowerRate(25.0, "Premium shower rate", 2);

        entityManager.persist(standardRate);
        entityManager.persist(premiumRate);
        entityManager.flush();

        // Act
        List<ShowerRate> rates = showerRateRepository.findAll();

        // Assert
        assertEquals(2, rates.size());
        assertTrue(rates.stream().anyMatch(rate -> rate.getRate() == 15.0));
        assertTrue(rates.stream().anyMatch(rate -> rate.getRate() == 25.0));
        assertTrue(rates.stream().anyMatch(rate -> rate.getDescription().equals("Standard shower rate")));
        assertTrue(rates.stream().anyMatch(rate -> rate.getDescription().equals("Premium shower rate")));
    }
}
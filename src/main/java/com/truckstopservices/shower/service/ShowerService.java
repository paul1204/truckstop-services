package com.truckstopservices.shower.service;

import com.truckstopservices.shower.dto.ShowerRequest;
import com.truckstopservices.shower.dto.ShowerResponse;
import com.truckstopservices.shower.entity.Shower;
import com.truckstopservices.shower.entity.ShowerRate;
import com.truckstopservices.shower.exception.ShowerException;
import com.truckstopservices.shower.repository.ShowerRepository;
import com.truckstopservices.shower.repository.ShowerRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowerService {

    private static final Logger logger = LoggerFactory.getLogger(ShowerService.class);
    private final ShowerRepository showerRepository;
    private final ShowerRateRepository showerRateRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final int CLEANING_MINUTES = 10;

    public ShowerService(ShowerRepository showerRepository, ShowerRateRepository showerRateRepository) {
        this.showerRepository = showerRepository;
        this.showerRateRepository = showerRateRepository;
    }

    public List<ShowerResponse> getAllShowers() {
        return showerRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowerResponse> getAvailableShowers() {
        return showerRepository.findAvailableShowers().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ShowerResponse getShowerByNumber(String showerNumber) {
        Shower shower = showerRepository.findByShowerNumber(showerNumber)
                .orElseThrow(() -> new ShowerException("Shower not found with number: " + showerNumber));
        return convertToResponse(shower);
    }

    @Transactional
    public ShowerResponse createShowerReservation(ShowerRequest request) {
        // Check if shower exists
        Shower shower = showerRepository.findByShowerNumber(request.showerNumber())
                .orElseGet(() -> {
                    Shower newShower = new Shower(request.showerNumber(), false, false, null, null, null, null);
                    return showerRepository.save(newShower);
                });

        // Check if shower is available
        if (shower.isOccupied() || shower.isCleaning()) {
            String status = shower.isOccupied() ? "occupied" : "being cleaned";
            return new ShowerResponse(
                shower.getId(),
                shower.getShowerNumber(),
                shower.isOccupied(),
                shower.isCleaning(),
                shower.getCustomerName(),
                shower.getOccupiedSince(),
                shower.getReservedUntil(),
                shower.getCleaningUntil(),
                0.0,
                "Shower is currently " + status
            );
        }

        // Validate reservation time (max 1 hour)
        LocalDateTime startTime = LocalDateTime.parse(request.startTime(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(request.endTime(), formatter);

        // Get the shower rate
        ShowerRate rate = showerRateRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ShowerException("Shower rate not found"));

        // Check if reservation exceeds max hours
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        if (hours > rate.getMaxHours()) {
            throw new ShowerException("Shower reservation cannot exceed " + rate.getMaxHours() + " hour(s)");
        }

        // Create reservation by updating shower
        shower.setOccupied(true);
        shower.setCleaning(false);
        shower.setCustomerName(request.customerName());
        shower.setOccupiedSince(request.startTime());
        shower.setReservedUntil(request.endTime());
        shower.setCleaningUntil(null);

        Shower showerWithReservation = showerRepository.save(shower);

        // Calculate cost
        double totalCost = calculateCost(request.startTime(), request.endTime(), rate);

        return new ShowerResponse(
            showerWithReservation.getId(),
            showerWithReservation.getShowerNumber(),
            showerWithReservation.isOccupied(),
            showerWithReservation.isCleaning(),
            showerWithReservation.getCustomerName(),
            showerWithReservation.getOccupiedSince(),
            showerWithReservation.getReservedUntil(),
            showerWithReservation.getCleaningUntil(),
            totalCost,
            "Shower reservation created successfully"
        );
    }

    @Transactional
    public ShowerResponse releaseShower(String showerNumber) {
        Shower shower = showerRepository.findByShowerNumber(showerNumber)
                .orElseThrow(() -> new ShowerException("Shower not found with number: " + showerNumber));

        if (!shower.isOccupied()) {
            return new ShowerResponse(
                shower.getId(),
                shower.getShowerNumber(),
                false,
                shower.isCleaning(),
                null,
                null,
                null,
                shower.getCleaningUntil(),
                0.0,
                "Shower is already vacant"
            );
        }

        // Store values before clearing
        String customerName = shower.getCustomerName();
        String occupiedSince = shower.getOccupiedSince();
        String reservedUntil = shower.getReservedUntil();

        // Set shower to cleaning state
        shower.setOccupied(false);
        shower.setCleaning(true);
        shower.setCustomerName(null);
        shower.setOccupiedSince(null);
        shower.setReservedUntil(null);

        // Set cleaning until time (current time + 10 minutes)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cleaningUntil = now.plusMinutes(CLEANING_MINUTES);
        shower.setCleaningUntil(cleaningUntil.format(formatter));

        // Log notification to cleaners
        logger.info("Cleaner notified: Shower {} needs cleaning after use by customer {}", 
                    showerNumber, customerName);

        Shower savedShower = showerRepository.save(shower);

        return new ShowerResponse(
            savedShower.getId(),
            savedShower.getShowerNumber(),
            false,
            true,
            customerName,
            occupiedSince,
            reservedUntil,
            savedShower.getCleaningUntil(),
            0.0,
            "Shower released, cleaners notified, and marked for cleaning until " + savedShower.getCleaningUntil()
        );
    }

    @Transactional
    public ShowerResponse markShowerClean(String showerNumber) {
        Shower shower = showerRepository.findByShowerNumber(showerNumber)
                .orElseThrow(() -> new ShowerException("Shower not found with number: " + showerNumber));

        if (!shower.isCleaning()) {
            return new ShowerResponse(
                shower.getId(),
                shower.getShowerNumber(),
                shower.isOccupied(),
                false,
                shower.getCustomerName(),
                shower.getOccupiedSince(),
                shower.getReservedUntil(),
                null,
                0.0,
                "Shower is not in cleaning state"
            );
        }

        // Clear the cleaning state
        shower.setCleaning(false);
        shower.setCleaningUntil(null);

        Shower savedShower = showerRepository.save(shower);

        return new ShowerResponse(
            savedShower.getId(),
            savedShower.getShowerNumber(),
            false,
            false,
            null,
            null,
            null,
            null,
            0.0,
            "Shower cleaning completed and is now available"
        );
    }

    private double calculateCost(String startTimeStr, String endTimeStr, ShowerRate rate) {
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        // If less than an hour, round up to an hour
        if (hours == 0) hours = 1;

        return hours * rate.getRate();
    }

    private ShowerResponse convertToResponse(Shower shower) {
        return new ShowerResponse(
            shower.getId(),
            shower.getShowerNumber(),
            shower.isOccupied(),
            shower.isCleaning(),
            shower.getCustomerName(),
            shower.getOccupiedSince(),
            shower.getReservedUntil(),
            shower.getCleaningUntil(),
            0.0, // Cost calculation requires rate and time
            ""
        );
    }
}

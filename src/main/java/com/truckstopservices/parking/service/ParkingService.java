package com.truckstopservices.parking.service;

import com.truckstopservices.parking.dto.ParkingRequest;
import com.truckstopservices.parking.dto.ParkingResponse;
import com.truckstopservices.parking.entity.ParkingRate;
import com.truckstopservices.parking.entity.ParkingRate.RateType;
import com.truckstopservices.parking.entity.ParkingSpot;
import com.truckstopservices.parking.repository.ParkingRateRepository;
import com.truckstopservices.parking.repository.ParkingSpotRepository;
import com.truckstopservices.parking.exception.ParkingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingRateRepository parkingRateRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    public ParkingService(ParkingSpotRepository parkingSpotRepository, ParkingRateRepository parkingRateRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingRateRepository = parkingRateRepository;
    }

    public List<ParkingResponse> getAllParkingSpots() {
        return parkingSpotRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ParkingResponse> getAvailableParkingSpots() {
        return parkingSpotRepository.findAvailableSpots().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ParkingResponse getParkingSpotByNumber(String spotNumber) {
        ParkingSpot parkingSpot = parkingSpotRepository.findBySpotNumber(spotNumber)
                .orElseThrow(() -> new ParkingException("Parking spot not found with number: " + spotNumber));
        return convertToResponse(parkingSpot);
    }

    @Transactional
    public ParkingResponse createParkingSpotReservation(ParkingRequest request) {
        // Check if spot exists
        ParkingSpot parkingSpot = parkingSpotRepository.findBySpotNumber(request.spotNumber())
                .orElseThrow(() -> new ParkingException("Parking Spot Not Found"));

        // Check if spot is available
        if (parkingSpot.isOccupied()) {
            return new ParkingResponse(
                parkingSpot.getId(),
                parkingSpot.getSpotNumber(),
                true,
                parkingSpot.getVehicleRegistration(),
                null,
                parkingSpot.getOccupiedSince(),
                parkingSpot.getReservedUntil(),
                0.0,
                "Spot is already occupied"
            );
        }

        // Get rate
        ParkingRate rate = parkingRateRepository.findByRateType(request.rateType())
                .orElseThrow(() -> new ParkingException("Rate not found for type: " + request.rateType()));

        // Create reservation by updating parking spot
        parkingSpot.setOccupied(true);
        parkingSpot.setVehicleRegistration(request.vehicleRegistration());
        parkingSpot.setOccupiedSince(request.startTime());
        parkingSpot.setReservedUntil(request.endTime());

        ParkingSpot spotWithReservation = parkingSpotRepository.save(parkingSpot);

        // Calculate cost
        double totalCost = calculateCost(request.startTime(), request.endTime(), rate);

        return new ParkingResponse(
            spotWithReservation.getId(),
            spotWithReservation.getSpotNumber(),
            spotWithReservation.isOccupied(),
            spotWithReservation.getVehicleRegistration(),
            request.rateType(),
            spotWithReservation.getOccupiedSince(),
            spotWithReservation.getReservedUntil(),
            totalCost,
            "Parking spot reservation created successfully"
        );
    }

    @Transactional
    public ParkingResponse releaseParkingSpot(String spotNumber) {
        ParkingSpot parkingSpot = parkingSpotRepository.findBySpotNumber(spotNumber)
                .orElseThrow(() -> new ParkingException("Parking spot not found with number: " + spotNumber));

        if (!parkingSpot.isOccupied()) {
            return new ParkingResponse(
                parkingSpot.getId(),
                parkingSpot.getSpotNumber(),
                false,
                null,
                null,
                null,
                null,
                0.0,
                "Spot is already vacant"
            );
        }

        // Store values before clearing
        String vehicleRegistration = parkingSpot.getVehicleRegistration();
        String occupiedSince = parkingSpot.getOccupiedSince();
        String reservedUntil = parkingSpot.getReservedUntil();

        // Clear the spot
        parkingSpot.setOccupied(false);
        parkingSpot.setVehicleRegistration(null);
        parkingSpot.setOccupiedSince(null);
        parkingSpot.setReservedUntil(null);

        ParkingSpot savedSpot = parkingSpotRepository.save(parkingSpot);

        return new ParkingResponse(
            savedSpot.getId(),
            savedSpot.getSpotNumber(),
            false,
            vehicleRegistration,
            null,
            occupiedSince,
            reservedUntil,
            0.0,
            "Parking spot released successfully"
        );
    }

    private double calculateCost(String startTimeStr, String endTimeStr, ParkingRate rate) {
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        long duration;
        double totalCost;

        switch (rate.getRateType()) {
            case HOURLY:
                duration = ChronoUnit.HOURS.between(startTime, endTime);
                totalCost = duration * rate.getRate();
                break;
            case NIGHTLY:
                duration = ChronoUnit.DAYS.between(startTime, endTime);
                totalCost = duration * rate.getRate();
                break;
            case WEEKLY:
                duration = ChronoUnit.WEEKS.between(startTime, endTime);
                // If less than a week, round up to a week
                if (duration == 0) duration = 1;
                totalCost = duration * rate.getRate();
                break;
            default:
                totalCost = 0.0;
        }

        return totalCost;
    }

    private ParkingResponse convertToResponse(ParkingSpot parkingSpot) {
        return new ParkingResponse(
            parkingSpot.getId(),
            parkingSpot.getSpotNumber(),
            parkingSpot.isOccupied(),
            parkingSpot.getVehicleRegistration(),
            null, // Rate type not stored in entity
            parkingSpot.getOccupiedSince(),
            parkingSpot.getReservedUntil(),
            0.0, // Cost calculation requires rate and time
            ""
        );
    }
}

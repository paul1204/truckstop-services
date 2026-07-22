package com.truckstopservices.parking.controller;

import com.truckstopservices.parking.dto.ParkingRequest;
import com.truckstopservices.parking.dto.ParkingResponse;
import com.truckstopservices.parking.entity.ParkingRate.RateType;
import com.truckstopservices.parking.exception.ParkingException;
import com.truckstopservices.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/spots")
    public ResponseEntity<List<ParkingResponse>> getAllParkingSpots() {
        List<ParkingResponse> spots = parkingService.getAllParkingSpots();
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/spots/available")
    public ResponseEntity<List<ParkingResponse>> getAvailableParkingSpots() {
        List<ParkingResponse> spots = parkingService.getAvailableParkingSpots();
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/spots/{spotNumber}")
    public ResponseEntity<ParkingResponse> getParkingSpotByNumber(@PathVariable String spotNumber) {
        ParkingResponse spot = parkingService.getParkingSpotByNumber(spotNumber);
        return ResponseEntity.ok(spot);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ParkingResponse> createParkingSpotReservation(@RequestBody ParkingRequest request) {
        ParkingResponse response = parkingService.createParkingSpotReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/release/{spotNumber}")
    public ResponseEntity<ParkingResponse> releaseParkingSpot(@PathVariable String spotNumber) {
        ParkingResponse response = parkingService.releaseParkingSpot(spotNumber);
        return ResponseEntity.ok(response);
    }

}

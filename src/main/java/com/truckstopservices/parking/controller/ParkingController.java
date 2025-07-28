package com.truckstopservices.parking.controller;

import com.truckstopservices.parking.dto.ParkingRequest;
import com.truckstopservices.parking.dto.ParkingResponse;
import com.truckstopservices.parking.entity.ParkingRate;
import com.truckstopservices.parking.entity.ParkingRate.RateType;
import com.truckstopservices.parking.entity.ParkingSpot;
import com.truckstopservices.parking.exception.ParkingException;
import com.truckstopservices.parking.repository.ParkingRateRepository;
import com.truckstopservices.parking.repository.ParkingSpotRepository;
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
    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingRateRepository parkingRateRepository;

    @Autowired
    public ParkingController(ParkingService parkingService, 
                            ParkingSpotRepository parkingSpotRepository,
                            ParkingRateRepository parkingRateRepository) {
        this.parkingService = parkingService;
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingRateRepository = parkingRateRepository;
    }

    @GetMapping("/spots")
    public ResponseEntity<List<ParkingResponse>> getAllParkingSpots() {
        List<ParkingResponse> spots = parkingService.getAllParkingSpots();
        return new ResponseEntity<>(spots, HttpStatus.OK);
    }

    @GetMapping("/spots/available")
    public ResponseEntity<List<ParkingResponse>> getAvailableParkingSpots() {
        List<ParkingResponse> spots = parkingService.getAvailableParkingSpots();
        return new ResponseEntity<>(spots, HttpStatus.OK);
    }

    @GetMapping("/spots/{spotNumber}")
    public ResponseEntity<ParkingResponse> getParkingSpotByNumber(@PathVariable String spotNumber) {
        try {
            ParkingResponse spot = parkingService.getParkingSpotByNumber(spotNumber);
            return new ResponseEntity<>(spot, HttpStatus.OK);
        } catch (ParkingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<ParkingResponse> createParkingSpotReservation(@RequestBody ParkingRequest request) {
        try {
            ParkingResponse response = parkingService.createParkingSpotReservation(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ParkingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/release/{spotNumber}")
    public ResponseEntity<ParkingResponse> releaseParkingSpot(@PathVariable String spotNumber) {
        try {
            ParkingResponse response = parkingService.releaseParkingSpot(spotNumber);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ParkingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

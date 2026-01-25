package com.truckstopservices.shower.controller;

import com.truckstopservices.shower.dto.ShowerRequest;
import com.truckstopservices.shower.dto.ShowerResponse;
import com.truckstopservices.shower.exception.ShowerException;
import com.truckstopservices.shower.repository.ShowerRepository;
import com.truckstopservices.shower.repository.ShowerRateRepository;
import com.truckstopservices.shower.service.ShowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/showers")
public class ShowerController {

    private final ShowerService showerService;
    private final ShowerRepository showerRepository;
    private final ShowerRateRepository showerRateRepository;

    public ShowerController(ShowerService showerService, 
                           ShowerRepository showerRepository,
                           ShowerRateRepository showerRateRepository) {
        this.showerService = showerService;
        this.showerRepository = showerRepository;
        this.showerRateRepository = showerRateRepository;
    }

    @GetMapping
    public ResponseEntity<List<ShowerResponse>> getAllShowers() {
        List<ShowerResponse> showers = showerService.getAllShowers();
        return ResponseEntity.ok(showers);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ShowerResponse>> getAvailableShowers() {
        List<ShowerResponse> showers = showerService.getAvailableShowers();
        return ResponseEntity.ok(showers);
    }

    @GetMapping("/{showerNumber}")
    public ResponseEntity<ShowerResponse> getShowerByNumber(@PathVariable String showerNumber) {
        ShowerResponse shower = showerService.getShowerByNumber(showerNumber);
        return ResponseEntity.ok(shower);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ShowerResponse> createShowerReservation(@RequestBody ShowerRequest request) {
        ShowerResponse response = showerService.createShowerReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/release/{showerNumber}")
    public ResponseEntity<ShowerResponse> releaseShower(@PathVariable String showerNumber) {
        ShowerResponse response = showerService.releaseShower(showerNumber);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/clean/{showerNumber}")
    public ResponseEntity<ShowerResponse> markShowerClean(@PathVariable String showerNumber) {
        ShowerResponse response = showerService.markShowerClean(showerNumber);
        return ResponseEntity.ok(response);
    }
}
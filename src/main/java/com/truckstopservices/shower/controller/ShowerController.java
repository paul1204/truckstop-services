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

    @Autowired
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
        return new ResponseEntity<>(showers, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ShowerResponse>> getAvailableShowers() {
        List<ShowerResponse> showers = showerService.getAvailableShowers();
        return new ResponseEntity<>(showers, HttpStatus.OK);
    }

    @GetMapping("/{showerNumber}")
    public ResponseEntity<ShowerResponse> getShowerByNumber(@PathVariable String showerNumber) {
        try {
            ShowerResponse shower = showerService.getShowerByNumber(showerNumber);
            return new ResponseEntity<>(shower, HttpStatus.OK);
        } catch (ShowerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<ShowerResponse> createShowerReservation(@RequestBody ShowerRequest request) {
        try {
            ShowerResponse response = showerService.createShowerReservation(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ShowerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/release/{showerNumber}")
    public ResponseEntity<ShowerResponse> releaseShower(@PathVariable String showerNumber) {
        try {
            ShowerResponse response = showerService.releaseShower(showerNumber);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ShowerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/clean/{showerNumber}")
    public ResponseEntity<ShowerResponse> markShowerClean(@PathVariable String showerNumber) {
        try {
            ShowerResponse response = showerService.markShowerClean(showerNumber);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ShowerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
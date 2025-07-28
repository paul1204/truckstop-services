package com.truckstopservices.parking.config;

import com.truckstopservices.parking.entity.ParkingSpot;
import com.truckstopservices.parking.entity.ParkingRate;
import com.truckstopservices.parking.entity.ParkingRate.RateType;
import com.truckstopservices.parking.repository.ParkingSpotRepository;
import com.truckstopservices.parking.repository.ParkingRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParkingInitializer {
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    
    @Autowired
    private ParkingRateRepository parkingRateRepository;
    
    @Bean
    public CommandLineRunner loadParkingData() {
        return args -> {
            // Initialize parking rates
            parkingRateRepository.save(new ParkingRate(RateType.HOURLY, 5.99, "Standard hourly rate"));
            parkingRateRepository.save(new ParkingRate(RateType.NIGHTLY, 29.99, "Overnight parking"));
            parkingRateRepository.save(new ParkingRate(RateType.WEEKLY, 149.99, "Weekly rate package"));
            
            // Initialize parking spots (e.g., 20 spots)
            for (int i = 1; i <= 20; i++) {
                String spotNumber = "A" + String.format("%02d", i);
                parkingSpotRepository.save(new ParkingSpot(spotNumber, false, null, null, null));
            }
        };
    }
}
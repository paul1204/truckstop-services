package com.truckstopservices.shower.config;

import com.truckstopservices.shower.entity.Shower;
import com.truckstopservices.shower.entity.ShowerRate;
import com.truckstopservices.shower.repository.ShowerRepository;
import com.truckstopservices.shower.repository.ShowerRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShowerInitializer {
    @Autowired
    private ShowerRepository showerRepository;
    
    @Autowired
    private ShowerRateRepository showerRateRepository;
    
    @Bean
    public CommandLineRunner loadShowerData() {
        return args -> {
            // Initialize shower rate ($25 per hour, 1 hour limit)
            showerRateRepository.save(new ShowerRate(25.0, "Standard shower rate (1 hour limit)", 1));
            
            // Initialize showers (e.g., 10 showers)
            for (int i = 1; i <= 10; i++) {
                String showerNumber = "S" + String.format("%02d", i);
                showerRepository.save(new Shower(showerNumber, false, false, null, null, null, null));
            }
        };
    }
}
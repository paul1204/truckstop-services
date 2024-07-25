package com.truckstopservices.inventory.fuel.testConfig;

import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.MidGradeOctane;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import com.truckstopservices.inventory.fuel.repository.FuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FuelInitializer {
    @Autowired
    private FuelRepository fuelRepository;

    double baseGasPrice = 1.99;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            fuelRepository.save(new Diesel(40, 2.99));
            fuelRepository.save(new RegularOctane(87, baseGasPrice ));
            fuelRepository.save(new MidGradeOctane(89, (baseGasPrice + 0.25)));
            fuelRepository.save(new PremiumOctane(91, (baseGasPrice + 0.50)));
        };
    }
}

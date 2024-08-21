package com.truckstopservices.inventory.fuel.testConfig;

import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.MidGradeOctane;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import com.truckstopservices.inventory.fuel.repository.DieselRepository;
//import com.truckstopservices.inventory.fuel.repository.FuelRepository;
import com.truckstopservices.inventory.fuel.repository.MidGradeFuelRepository;
import com.truckstopservices.inventory.fuel.repository.PremimumFuelRepository;
import com.truckstopservices.inventory.fuel.repository.RegularFuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FuelInventoryInitializer {
    @Autowired
    private DieselRepository dieselRepository;

    @Autowired
    private RegularFuelRepository regularFuelRepository;

    @Autowired
    private MidGradeFuelRepository midGradeFuelRepository;

    @Autowired
    private PremimumFuelRepository premimumFuelRepository;

    double baseGasPrice = 1.99;

    @Bean
    public CommandLineRunner loadFuelInventoryData() {
        return args -> {
            dieselRepository.save(new Diesel(40, 2.99, 100));
            regularFuelRepository.save(new RegularOctane(87, baseGasPrice ,100));
            midGradeFuelRepository.save(new MidGradeOctane(89, (baseGasPrice + 0.25), 100));
            premimumFuelRepository.save(new PremiumOctane(91, (baseGasPrice + 0.50),100));
        };
    }
}

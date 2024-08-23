package com.truckstopservices.inventory.merchandise.config;

import com.truckstopservices.inventory.merchandise.beverages.entity.ColdBeverage;
import com.truckstopservices.inventory.merchandise.nonRestaurant.entity.NonRestaurantFood;
import com.truckstopservices.inventory.merchandise.repository.BeverageRepository;
import com.truckstopservices.inventory.merchandise.repository.NonRestaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MerchandiseInventoryInitalizer {

    @Autowired
    BeverageRepository beverageRepository;

    @Autowired
    NonRestaurant nonRestaurantRepository;

    @Bean
    public CommandLineRunner loadMerchandiseInventoryData() {
        return args -> {
        beverageRepository.save(new ColdBeverage("100", "Coke", 1.99, "Coca-Cola", 20));
        beverageRepository.save(new ColdBeverage("101", "Diet Coke", 1.99, "Coca-Cola", 20));

        nonRestaurantRepository.save(new NonRestaurantFood("500", "Chips", 1.00, "Lays", 20));
        nonRestaurantRepository.save(new NonRestaurantFood("501", "Spicy Chips", 1.00, "Lays", 20));
        };
    }
}

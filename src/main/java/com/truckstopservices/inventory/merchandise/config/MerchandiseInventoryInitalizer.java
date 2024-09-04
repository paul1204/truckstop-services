package com.truckstopservices.inventory.merchandise.config;

import com.truckstopservices.inventory.merchandise.beverages.entity.ColdBeverage;
import com.truckstopservices.inventory.merchandise.nonRestaurant.entity.NonRestaurantFood;
import com.truckstopservices.inventory.merchandise.repository.BeverageRepository;
import com.truckstopservices.inventory.merchandise.repository.NonRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MerchandiseInventoryInitalizer {

    @Autowired
    BeverageRepository beverageRepository;

    @Autowired
    NonRestaurantRepository nonRestaurantRepository;

    @Bean
    public CommandLineRunner loadMerchandiseInventoryData() {
        return args -> {
        beverageRepository.save(new ColdBeverage("100", "Coke", 1.99, "Coca-Cola", 20, "20oz"));
        beverageRepository.save(new ColdBeverage("101", "Diet Coke", 1.99, "Coca-Cola", 20, "20oz"));
        beverageRepository.save(new ColdBeverage("102", "Sprite", 1.99, "Coca-Cola", 20, "20oz"));
        beverageRepository.save(new ColdBeverage("103", "Ginger Ale", 1.99, "Coca-Cola", 20, "20oz"));
        beverageRepository.save(new ColdBeverage("104", "Sprite Zero", 1.99, "Coca-Cola", 20, "20oz"));

        nonRestaurantRepository.save(new NonRestaurantFood("999", "Chips", 1.00, "Lays", 20, "M"));
        nonRestaurantRepository.save(new NonRestaurantFood("998", "Spicy Chips", 1.00, "Lays", 20, "M"));
        nonRestaurantRepository.save(new NonRestaurantFood("997", "Cookies", 1.00, "Little Debbie", 20, "12pk"));
        nonRestaurantRepository.save(new NonRestaurantFood("996", "Donut", 1.00, "Little Debbie", 20, "6pk"));
        nonRestaurantRepository.save(new NonRestaurantFood("995", "Reese's Cup", 1.00, "Hershey", 20, "M"));
        };
    }
}

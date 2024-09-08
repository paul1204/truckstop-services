package com.truckstopservices.inventory.restaurant.config;

import com.truckstopservices.inventory.restaurant.entity.HotFood;
import com.truckstopservices.inventory.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantInitalizer {
    @Autowired
    private RestaurantRepository restaurantRepository;

    final double taxRate = 0.05;
    @Bean
    public CommandLineRunner loadRestaurantInventoryData(){
        return args -> {
            restaurantRepository.save(new HotFood("HF123", "Cheese Pizza", 10, "Large" , 7.99));
            restaurantRepository.save(new HotFood("HF124", "Pepperoni Pizza", 10, "Large" , 8.99));

            restaurantRepository.save(new HotFood("HF125", "Quarter Pound Hot Dog", 20, "1/4 Lbs" , 1.99));
            restaurantRepository.save(new HotFood("HF126", "Double Quarter Pound Hot Dog", 20, "1/2 Lbs" , 2.99));

            restaurantRepository.save(new HotFood("HF127", "Sausage Egg and Chesse Biscuit", 20, "1 Biscuit" , 1.99));
            restaurantRepository.save(new HotFood("HF128", "Chicken  Biscuit", 20, "1 Biscuit" , 1.99));
            restaurantRepository.save(new HotFood("HF129", "Bacon Egg and Cheese  Biscuit", 20, "1 Biscuit" , 1.99));
        };
    }

}

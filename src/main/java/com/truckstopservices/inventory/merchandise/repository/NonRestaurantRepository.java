package com.truckstopservices.inventory.merchandise.repository;

import com.truckstopservices.inventory.merchandise.beverages.entity.ColdBeverage;
import com.truckstopservices.inventory.merchandise.nonRestaurant.entity.NonRestaurantFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NonRestaurantRepository extends JpaRepository<NonRestaurantFood, Long> {
    Optional<NonRestaurantFood> findBySkuCode(String skuCode);
}

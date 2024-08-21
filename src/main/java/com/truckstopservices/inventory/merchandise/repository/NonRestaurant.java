package com.truckstopservices.inventory.merchandise.repository;

import com.truckstopservices.inventory.merchandise.nonRestaurant.entity.NonRestaurantFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonRestaurant extends JpaRepository<NonRestaurantFood, Long> {
}

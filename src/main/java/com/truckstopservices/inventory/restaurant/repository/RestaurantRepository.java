package com.truckstopservices.inventory.restaurant.repository;

import com.truckstopservices.inventory.restaurant.entity.HotFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<HotFood, Long> {

}

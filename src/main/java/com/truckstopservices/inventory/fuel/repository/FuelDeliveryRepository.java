package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelDeliveryRepository extends JpaRepository<FuelDelivery, Long> {
}

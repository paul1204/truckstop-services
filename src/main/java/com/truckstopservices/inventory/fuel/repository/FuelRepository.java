package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.model.FuelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelRepository extends JpaRepository<FuelModel, Double> {
}




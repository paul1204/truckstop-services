package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuelDeliveryRepository extends JpaRepository<FuelDelivery, Long>{
}
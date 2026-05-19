package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuelDeliveryRepository extends JpaRepository<FuelDelivery, Long>{
    List<FuelDelivery> findAllByOrderByIdDesc(Pageable pageable);
}
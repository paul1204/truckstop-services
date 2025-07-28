package com.truckstopservices.parking.repository;

import com.truckstopservices.parking.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findBySpotNumber(String spotNumber);
    
    @Query("SELECT p FROM ParkingSpot p WHERE p.occupied = false")
    List<ParkingSpot> findAvailableSpots();
    
    @Query("SELECT p FROM ParkingSpot p WHERE p.vehicleRegistration = ?1")
    Optional<ParkingSpot> findByVehicleRegistration(String vehicleRegistration);
}
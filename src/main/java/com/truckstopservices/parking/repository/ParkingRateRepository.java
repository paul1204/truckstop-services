package com.truckstopservices.parking.repository;

import com.truckstopservices.parking.entity.ParkingRate;
import com.truckstopservices.parking.entity.ParkingRate.RateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingRateRepository extends JpaRepository<ParkingRate, Long> {
    Optional<ParkingRate> findByRateType(RateType rateType);
}
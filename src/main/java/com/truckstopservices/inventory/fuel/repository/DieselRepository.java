package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.FuelDelivery;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DieselRepository extends JpaRepository<Diesel, Long> {
    Optional<Diesel> findByOctane(int octane);
    @Query("SELECT d FROM Diesel d WHERE d.active = TRUE ORDER BY id ASC LIMIT 1")
    Optional<Diesel> findFIFOAvailableGallons();

    @Query("SELECT d FROM Diesel d WHERE d.active = TRUE ORDER BY id ASC LIMIT 1 OFFSET 1")
    Optional<Diesel> findNextFifoNextAvailableGallons();
}

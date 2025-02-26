package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegularFuelRepository extends JpaRepository<RegularOctane, Long> {
    Optional<RegularOctane> findByOctane(int octane);
    @Query("SELECT r FROM RegularOctane r WHERE r.active = TRUE ORDER BY id ASC LIMIT 1")
    Optional<RegularOctane> findFIFOAvailableGallons();

    @Query("SELECT r FROM RegularOctane r WHERE r.active = TRUE ORDER BY id ASC LIMIT 1 OFFSET 1")
    Optional<RegularOctane> findNextFifoNextAvailableGallons();
}


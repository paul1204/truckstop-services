package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.MidGradeOctane;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PremimumFuelRepository extends JpaRepository<PremiumOctane, Long> {
    Optional<PremiumOctane> findByOctane(int octane);
    @Query("SELECT p FROM PremiumOctane p WHERE p.active = TRUE ORDER BY id ASC LIMIT 1")
    Optional<PremiumOctane> findFIFOAvailableGallons();

    @Query("SELECT p FROM PremiumOctane p WHERE p.active = TRUE ORDER BY id ASC LIMIT 1 OFFSET 1")
    Optional<PremiumOctane> findNextFifoNextAvailableGallons();
}


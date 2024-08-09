package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegularFuelRepository extends JpaRepository<RegularOctane, Double> {
    Optional<RegularOctane> findByOctane(int octane);
}


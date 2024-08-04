package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularFuelRepository extends JpaRepository<RegularOctane, Integer> {
}


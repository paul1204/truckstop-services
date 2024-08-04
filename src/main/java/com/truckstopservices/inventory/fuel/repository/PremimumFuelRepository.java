package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.MidGradeOctane;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremimumFuelRepository extends JpaRepository<PremiumOctane, Integer> {
}


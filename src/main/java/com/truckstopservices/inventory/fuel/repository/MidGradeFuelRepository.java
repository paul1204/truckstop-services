package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.MidGradeOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MidGradeFuelRepository extends JpaRepository<MidGradeOctane, Integer> {
}

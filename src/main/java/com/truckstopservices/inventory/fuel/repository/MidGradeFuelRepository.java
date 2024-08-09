package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.MidGradeOctane;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MidGradeFuelRepository extends JpaRepository<MidGradeOctane, Integer> {
    Optional<MidGradeOctane> findByOctane(int octane);
}

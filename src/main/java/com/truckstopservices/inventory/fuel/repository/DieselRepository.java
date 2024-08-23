package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.fuel.entity.RegularOctane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DieselRepository extends JpaRepository<Diesel, Long> {
    Optional<Diesel> findByOctane(int octane);
}

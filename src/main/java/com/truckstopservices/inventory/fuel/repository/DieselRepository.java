package com.truckstopservices.inventory.fuel.repository;

import com.truckstopservices.inventory.fuel.entity.Diesel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DieselRepository extends JpaRepository<Diesel, Integer> {
}

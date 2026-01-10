package com.truckstopservices.inventory.merchandise.repository;

import com.truckstopservices.inventory.merchandise.packagedfood.entity.PackagedFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackagedFoodRepository extends JpaRepository<PackagedFood, Long> {
    Optional<PackagedFood> findBySkuCode(String skuCode);
}

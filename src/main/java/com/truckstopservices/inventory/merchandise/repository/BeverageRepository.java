package com.truckstopservices.inventory.merchandise.repository;

import com.truckstopservices.inventory.fuel.entity.Diesel;
import com.truckstopservices.inventory.merchandise.beverages.entity.ColdBeverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeverageRepository extends JpaRepository<ColdBeverage, Long> {
    Optional<ColdBeverage> findBySkuCode(String skuCode);
}

package com.truckstopservices.inventory.merchandise.repository;

import com.truckstopservices.inventory.merchandise.beverages.entity.BottledBeverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BottledBeverageRepository extends JpaRepository<BottledBeverage, Long> {
    Optional<BottledBeverage> findBySkuCode(String skuCode);
}

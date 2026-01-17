package com.truckstopservices.inventory.merchandise.repository;

import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.merchandise.beverages.entity.BottledBeverage;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageCostByBrand;
import com.truckstopservices.inventory.merchandise.dto.BottledBeverageInventoryByBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BottledBeverageRepository extends JpaRepository<BottledBeverage, Long> {
    Optional<BottledBeverage> findBySkuCode(String skuCode);

    @Query(value = "SELECT b.brand AS brand, SUM(b.qty) AS qty FROM bottled_beverage b GROUP BY b.brand", nativeQuery = true)
    List<BottledBeverageInventoryByBrand> findInventoryByBrandSqlAgg();

    @Query(value = "SELECT b.brand AS brand, ROUND(SUM(b.price),2) AS price FROM bottled_beverage b GROUP BY b.brand ORDER BY price DESC", nativeQuery = true)
    List<BottledBeverageCostByBrand> returnInventoryCostByBrandSqlAgg();

}

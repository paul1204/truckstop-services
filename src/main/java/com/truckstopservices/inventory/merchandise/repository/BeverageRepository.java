package com.truckstopservices.inventory.merchandise.repository;

import com.truckstopservices.inventory.merchandise.beverages.entity.ColdBeverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeverageRepository extends JpaRepository<ColdBeverage, Long> {
}

package com.truckstopservices.shower.repository;

import com.truckstopservices.shower.entity.ShowerRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowerRateRepository extends JpaRepository<ShowerRate, Long> {

}
package com.truckstopservices.accounting.pos.repository;

import com.truckstopservices.accounting.pos.entity.POSDailyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface POSDailyTableRepository extends JpaRepository<POSDailyTable, String> {
    // Additional query methods can be added here if needed
}
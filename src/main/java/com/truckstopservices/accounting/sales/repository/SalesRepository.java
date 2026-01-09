package com.truckstopservices.accounting.sales.repository;

import com.truckstopservices.accounting.sales.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository<Sales, String> {
}

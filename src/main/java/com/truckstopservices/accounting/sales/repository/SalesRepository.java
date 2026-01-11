package com.truckstopservices.accounting.sales.repository;

import com.truckstopservices.accounting.sales.dto.SalesByShift;
import com.truckstopservices.accounting.sales.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, String> {

    @Query(value = "SELECT SUM(sales_amount) AS salesAmount, shift_number AS shiftNumber FROM sales GROUP BY shift_number ORDER BY shift_number ASC", nativeQuery = true)
    List<SalesByShift> findSalesByTodayAllShifts();
}

package com.truckstopservices.processing.repository;

import com.truckstopservices.processing.entity.ShiftReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.truckstopservices.processing.entity.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftReportRepository extends JpaRepository<ShiftReport, Long> {

    Optional<ShiftReport> findByShiftNumber(String shiftNumber);
    List<FuelSales> findFuelSalesByShiftId(Long id);
    List<MerchandiseSales> findMerchandiseSalesByShiftId(Long id);
    List<RestaurantSales> findRestaurantSalesByShiftId(Long id);
    List<TobaccoSales> findTobaccoSalesByShiftId(Long id);

}

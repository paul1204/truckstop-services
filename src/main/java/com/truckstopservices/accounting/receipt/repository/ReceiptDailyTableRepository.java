package com.truckstopservices.accounting.receipt.repository;

import com.truckstopservices.accounting.receipt.entity.ReceiptDailyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptDailyTableRepository extends JpaRepository<ReceiptDailyTable, String> {
}

package com.truckstopservices.accounting.houseaccount.repository;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for house account transaction operations.
 */
@Repository
public interface HouseAccountTransactionRepository extends JpaRepository<HouseAccountTransaction, Long> {
    
    /**
     * Find a transaction by its invoice number.
     * 
     * @param invoiceNumber The invoice number to search for
     * @return An optional containing the transaction if found
     */
    Optional<HouseAccountTransaction> findByInvoiceNumber(String invoiceNumber);
    
    /**
     * Find all transactions for a specific house account.
     * 
     * @param houseAccountId The house account ID to filter by
     * @return A list of transactions for the specified house account
     */
    List<HouseAccountTransaction> findByHouseAccountId(String houseAccountId);
    
    /**
     * Find transactions that occurred on a specific date.
     * 
     * @param dateOfPurchase The purchase date to filter by
     * @return A list of transactions that occurred on the specified date
     */
    List<HouseAccountTransaction> findByDateOfPurchase(LocalDate dateOfPurchase);
    
    /**
     * Find transactions that are due on a specific date.
     * 
     * @param dateDue The due date to filter by
     * @return A list of transactions that are due on the specified date
     */
    List<HouseAccountTransaction> findByDateDue(LocalDate dateDue);
    
    /**
     * Find transactions with an amount greater than or equal to the specified value.
     * 
     * @param amount The minimum amount to filter by
     * @return A list of transactions with amounts greater than or equal to the specified value
     */
    List<HouseAccountTransaction> findByAmountGreaterThanEqual(Double amount);
    
    /**
     * Find transactions with gallons greater than or equal to the specified value.
     * 
     * @param gallons The minimum gallons to filter by
     * @return A list of transactions with gallons greater than or equal to the specified value
     */
    List<HouseAccountTransaction> findByGallonsGreaterThanEqual(Double gallons);
    
    /**
     * Find transactions that occurred between the specified dates (inclusive).
     * 
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return A list of transactions that occurred between the specified dates
     */
    List<HouseAccountTransaction> findByDateOfPurchaseBetween(LocalDate startDate, LocalDate endDate);
}
package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccountTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for house account transaction operations.
 * Only provides create and read operations as transactions should not be updated or deleted.
 */
public interface HouseAccountTransactionService {
    
    /**
     * Create a new house account transaction.
     * 
     * @param transaction The house account transaction to create
     * @return The created house account transaction
     */
    HouseAccountTransaction createTransaction(HouseAccountTransaction transaction);
    
    /**
     * Get a transaction by its ID.
     * 
     * @param id The transaction ID
     * @return An optional containing the transaction if found
     */
    Optional<HouseAccountTransaction> getTransactionById(Long id);
    
    /**
     * Get a transaction by its invoice number.
     * 
     * @param invoiceNumber The invoice number
     * @return An optional containing the transaction if found
     */
    Optional<HouseAccountTransaction> getTransactionByInvoiceNumber(String invoiceNumber);
    
    /**
     * Get all transactions for a specific house account.
     * 
     * @param houseAccountId The house account ID
     * @return A list of transactions for the specified house account
     */
    List<HouseAccountTransaction> getTransactionsByHouseAccountId(String houseAccountId);
    
    /**
     * Get all transactions that occurred on a specific date.
     * 
     * @param dateOfPurchase The purchase date
     * @return A list of transactions that occurred on the specified date
     */
    List<HouseAccountTransaction> getTransactionsByDateOfPurchase(LocalDate dateOfPurchase);
    
    /**
     * Get all transactions that are due on a specific date.
     * 
     * @param dateDue The due date
     * @return A list of transactions that are due on the specified date
     */
    List<HouseAccountTransaction> getTransactionsByDateDue(LocalDate dateDue);
    
    /**
     * Get all transactions that occurred between the specified dates (inclusive).
     * 
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return A list of transactions that occurred between the specified dates
     */
    List<HouseAccountTransaction> getTransactionsBetweenDates(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get all transactions.
     * 
     * @return A list of all transactions
     */
    List<HouseAccountTransaction> getAllTransactions();
}
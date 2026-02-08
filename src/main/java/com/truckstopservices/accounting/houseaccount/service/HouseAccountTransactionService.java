package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccountTransaction;
import com.truckstopservices.accounting.houseaccount.repository.HouseAccountTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for house account transaction operations.
 * Only provides create and read operations as transactions should not be updated or deleted.
 */
@Service
public class HouseAccountTransactionService {
    
    private final HouseAccountTransactionRepository transactionRepository;
    
    public HouseAccountTransactionService(HouseAccountTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    /**
     * Create a new house account transaction.
     * 
     * @param transaction The house account transaction to create
     * @return The created house account transaction
     */
    @Transactional
    public HouseAccountTransaction createTransaction(HouseAccountTransaction transaction) {
        // Ensure dates are set correctly
        if (transaction.getDateOfPurchase() == null) {
            transaction.setDateOfPurchase(LocalDate.now());
        }
        
        if (transaction.getDateDue() == null) {
            transaction.setDateDue(transaction.getDateOfPurchase().plusDays(7));
        }
        
        return transactionRepository.save(transaction);
    }
    
    /**
     * Get a transaction by its ID.
     * 
     * @param id The transaction ID
     * @return An optional containing the transaction if found
     */
    @Transactional(readOnly = true)
    public Optional<HouseAccountTransaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
    /**
     * Get a transaction by its invoice number.
     * 
     * @param invoiceNumber The invoice number
     * @return An optional containing the transaction if found
     */
    @Transactional(readOnly = true)
    public Optional<HouseAccountTransaction> getTransactionByInvoiceNumber(String invoiceNumber) {
        return transactionRepository.findByInvoiceNumber(invoiceNumber);
    }
    
    /**
     * Get all transactions for a specific house account.
     * 
     * @param houseAccountId The house account ID
     * @return A list of transactions for the specified house account
     */
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getTransactionsByHouseAccountId(String houseAccountId) {
        return transactionRepository.findByHouseAccountId(houseAccountId);
    }
    
    /**
     * Get all transactions that occurred on a specific date.
     * 
     * @param dateOfPurchase The purchase date
     * @return A list of transactions that occurred on the specified date
     */
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getTransactionsByDateOfPurchase(LocalDate dateOfPurchase) {
        return transactionRepository.findByDateOfPurchase(dateOfPurchase);
    }
    
    /**
     * Get all transactions that are due on a specific date.
     * 
     * @param dateDue The due date
     * @return A list of transactions that are due on the specified date
     */
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getTransactionsByDateDue(LocalDate dateDue) {
        return transactionRepository.findByDateDue(dateDue);
    }
    
    /**
     * Get all transactions that occurred between the specified dates (inclusive).
     * 
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return A list of transactions that occurred between the specified dates
     */
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateOfPurchaseBetween(startDate, endDate);
    }
    
    /**
     * Get all transactions.
     * 
     * @return A list of all transactions
     */
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
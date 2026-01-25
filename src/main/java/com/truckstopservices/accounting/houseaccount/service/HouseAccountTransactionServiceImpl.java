package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccountTransaction;
import com.truckstopservices.accounting.houseaccount.repository.HouseAccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the HouseAccountTransactionService interface.
 * Only provides create and read operations as transactions should not be updated or deleted.
 */
@Service
public class HouseAccountTransactionServiceImpl implements HouseAccountTransactionService {
    
    private final HouseAccountTransactionRepository transactionRepository;
    
    public HouseAccountTransactionServiceImpl(HouseAccountTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    @Override
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
    
    @Override
    @Transactional(readOnly = true)
    public Optional<HouseAccountTransaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<HouseAccountTransaction> getTransactionByInvoiceNumber(String invoiceNumber) {
        return transactionRepository.findByInvoiceNumber(invoiceNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getTransactionsByHouseAccountId(String houseAccountId) {
        return transactionRepository.findByHouseAccountId(houseAccountId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getTransactionsByDateOfPurchase(LocalDate dateOfPurchase) {
        return transactionRepository.findByDateOfPurchase(dateOfPurchase);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getTransactionsByDateDue(LocalDate dateDue) {
        return transactionRepository.findByDateDue(dateDue);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateOfPurchaseBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
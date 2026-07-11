package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
import com.truckstopservices.accounting.houseaccount.dto.HouseAccountResponse;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;
import com.truckstopservices.accounting.houseaccount.exception.HouseAccountException;
import com.truckstopservices.accounting.houseaccount.repository.HouseAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseAccountService {
    
    private final HouseAccountRepository houseAccountRepository;
    
    public HouseAccountService(HouseAccountRepository houseAccountRepository) {
        this.houseAccountRepository = houseAccountRepository;
    }
    
    /**
     * Create a new house account.
     * 
     * @param request The house account request
     * @return The created house account response
     */
    @Transactional
    public HouseAccountResponse createHouseAccount(HouseAccountRequest request) {
        // Create new account with auto-generated ID and company name
        String companyName = request.getCompanyName();
        if (companyName == null || companyName.isEmpty()) {
            // Generate a default company name if not provided
            companyName = "Company-" + System.currentTimeMillis();
        }
        
        HouseAccount houseAccount = new HouseAccount(
            companyName,
            request.getPhoneNumber(),
            request.getAddress()
        );

        // Set credit limit if provided, otherwise use default
        if (request.getCreditLimit() != null) {
            houseAccount.setCreditLimit(request.getCreditLimit());
        }

        // Save and return
        HouseAccount savedAccount = houseAccountRepository.save(houseAccount);
        return HouseAccountResponse.fromEntity(savedAccount);
    }
    
    /**
     * Get a house account by ID.
     * 
     * @param id The account ID
     * @return The house account response
     */
    @Transactional(readOnly = true)
    public HouseAccountResponse getHouseAccount(String id) {
        HouseAccount houseAccount = findAccountById(id);
        return HouseAccountResponse.fromEntity(houseAccount);
    }
    
    /**
     * Get all house accounts.
     * 
     * @return A list of all house account responses
     */
    @Transactional(readOnly = true)
    public List<HouseAccountResponse> getAllHouseAccounts() {
        return houseAccountRepository.findAll().stream()
            .map(HouseAccountResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Update an existing house account.
     * 
     * @param id The account ID
     * @param request The house account request
     * @return The updated house account response
     */
    @Transactional
    public HouseAccountResponse updateHouseAccount(String id, HouseAccountRequest request) {
        HouseAccount houseAccount = findAccountById(id);
        
        // Update fields if provided

        if (request.getPhoneNumber() != null) {
            houseAccount.setPhoneNumber(request.getPhoneNumber());
        }
        
        if (request.getAddress() != null) {
            houseAccount.setAddress(request.getAddress());
        }
        
        if (request.getCreditLimit() != null) {
            houseAccount.setCreditLimit(request.getCreditLimit());
        }
        
        if (request.getCompanyName() != null) {
            houseAccount.setCompanyName(request.getCompanyName());
        }
        
        houseAccount.setUpdatedAt(LocalDateTime.now());
        
        // Save and return
        HouseAccount updatedAccount = houseAccountRepository.save(houseAccount);
        return HouseAccountResponse.fromEntity(updatedAccount);
    }
    
    /**
     * Delete a house account.
     * 
     * @param id The account ID
     */
    @Transactional
    public void deleteHouseAccount(String id) {
        if (!houseAccountRepository.existsById(id)) {
            throw HouseAccountException.accountNotFound(id);
        }
        
        houseAccountRepository.deleteById(id);
    }
    
    /**
     * Update the standing status of a house account.
     * 
     * @param id The account ID
     * @param standing The new standing status
     * @return The updated house account response
     */
    @Transactional
    public HouseAccountResponse updateAccountStanding(String id, AccountStanding standing) {
        HouseAccount houseAccount = findAccountById(id);
        
        // Update standing and handle reset of good standing duration if needed
        houseAccount.setAccountStanding(standing);
        
        // Save and return
        HouseAccount updatedAccount = houseAccountRepository.save(houseAccount);
        return HouseAccountResponse.fromEntity(updatedAccount);
    }
    
    /**
     * Adjust the balance of a house account with credit limit check.
     * 
     * @param id The account ID
     * @param amountToCharge The amount to charge to the account
     * @param gallonsToCharge The gallons to charge to the account
     * @return The updated house account response
     */
    @Transactional
    public HouseAccountResponse adjustBalance(String id, Double amountToCharge, Double gallonsToCharge) {
        HouseAccount houseAccount = findAccountById(id);
        
        validateCreditLimit(houseAccount, amountToCharge);
        
        houseAccount.setAmountDue(houseAccount.getAmountDue() + amountToCharge);
        houseAccount.setGallonsDue(houseAccount.getGallonsDue() + gallonsToCharge);
        houseAccount.setUpdatedAt(LocalDateTime.now());
        
        HouseAccount updatedAccount = houseAccountRepository.save(houseAccount);
        return HouseAccountResponse.fromEntity(updatedAccount);
    }
    
    /**
     * Validates that the account has enough credit limit for the attempt amount.
     * 
     * @param account The house account
     * @param attemptAmount The amount being charged
     * @throws HouseAccountException if credit limit is exceeded
     */
    private void validateCreditLimit(HouseAccount account, Double attemptAmount) {
        if (account.getAmountDue() + attemptAmount > account.getCreditLimit()) {
            double remainingCredit = Math.round((account.getCreditLimit() - account.getGallonsDue()) * 100.0) / 100.0;
            throw HouseAccountException.creditLimitExceeded(account.getHouseAccountId(), remainingCredit);
        }
    }
    
    /**
     * Find house accounts by standing status.
     * 
     * @param standing The standing status
     * @return A list of house account responses
     */
    @Transactional(readOnly = true)
    public List<HouseAccountResponse> findByAccountStanding(AccountStanding standing) {
        return houseAccountRepository.findByAccountStanding(standing).stream()
            .map(HouseAccountResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Find house accounts by customer number (case insensitive, partial match).
     * 
     * @param name The name to search for
     * @return A list of house account responses
     */
    @Transactional(readOnly = true)
    public List<HouseAccountResponse> findByName(String name) {
        return houseAccountRepository.findByCompanyName(name).stream()
            .map(HouseAccountResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Find house accounts by phone number.
     * 
     * @param phoneNumber The phone number
     * @return A list of house account responses
     */
    @Transactional(readOnly = true)
    public List<HouseAccountResponse> findByPhoneNumber(String phoneNumber) {
        return houseAccountRepository.findByPhoneNumber(phoneNumber).stream()
            .map(HouseAccountResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Update the credit limit of a house account.
     * 
     * @param id The account ID
     * @param creditLimit The new credit limit
     * @return The updated house account response
     */
    @Transactional
    public HouseAccountResponse updateCreditLimit(String id, Double creditLimit) {
        HouseAccount houseAccount = findAccountById(id);
        
        houseAccount.setCreditLimit(creditLimit);
        houseAccount.setUpdatedAt(LocalDateTime.now());
        
        HouseAccount updatedAccount = houseAccountRepository.save(houseAccount);
        return HouseAccountResponse.fromEntity(updatedAccount);
    }
    
    /**
     * Helper method to find an account by ID or throw an exception if not found.
     * 
     * @param id The account ID
     * @return The house account
     * @throws HouseAccountException if the account is not found
     */
    private HouseAccount findAccountById(String id) {
        return houseAccountRepository.findById(id)
            .orElseThrow(() -> HouseAccountException.accountNotFound(id));
    }
}
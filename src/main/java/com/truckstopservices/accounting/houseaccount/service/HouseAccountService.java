package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
import com.truckstopservices.accounting.houseaccount.dto.HouseAccountResponse;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;

import java.util.List;

/**
 * Service interface for house account operations.
 */
public interface HouseAccountService {
    
    /**
     * Create a new house account.
     * 
     * @param request The house account request
     * @return The created house account response
     */
    HouseAccountResponse createHouseAccount(HouseAccountRequest request);
    
    /**
     * Get a house account by customer number.
     * 
     * @param customerNumber The customer number
     * @return The house account response
     */
    HouseAccountResponse getHouseAccount(String customerNumber);
    
    /**
     * Get all house accounts.
     * 
     * @return A list of all house account responses
     */
    List<HouseAccountResponse> getAllHouseAccounts();
    
    /**
     * Update an existing house account.
     * 
     * @param customerNumber The customer number
     * @param request The house account request
     * @return The updated house account response
     */
    HouseAccountResponse updateHouseAccount(String customerNumber, HouseAccountRequest request);
    
    /**
     * Delete a house account.
     * 
     * @param customerNumber The customer number
     */
    void deleteHouseAccount(String customerNumber);
    
    /**
     * Update the standing status of a house account.
     * 
     * @param customerNumber The customer number
     * @param standing The new standing status
     * @return The updated house account response
     */
    HouseAccountResponse updateAccountStanding(String customerNumber, AccountStanding standing);
    
    /**
     * Find house accounts by standing status.
     * 
     * @param standing The standing status
     * @return A list of house account responses
     */
    List<HouseAccountResponse> findByAccountStanding(AccountStanding standing);
    
    /**
     * Find house accounts by name (case insensitive, partial match).
     * 
     * @param name The name to search for
     * @return A list of house account responses
     */
    List<HouseAccountResponse> findByName(String name);
    
    /**
     * Find house accounts by phone number.
     * 
     * @param phoneNumber The phone number
     * @return A list of house account responses
     */
    List<HouseAccountResponse> findByPhoneNumber(String phoneNumber);
    
    /**
     * Update the credit limit of a house account.
     * 
     * @param customerNumber The customer number
     * @param creditLimit The new credit limit
     * @return The updated house account response
     */
    HouseAccountResponse updateCreditLimit(String customerNumber, Double creditLimit);
}
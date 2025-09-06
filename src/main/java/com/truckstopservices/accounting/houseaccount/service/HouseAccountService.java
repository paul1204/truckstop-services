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
     * Get a house account by ID.
     * 
     * @param id The account ID
     * @return The house account response
     */
    HouseAccountResponse getHouseAccount(String id);
    
    /**
     * Get all house accounts.
     * 
     * @return A list of all house account responses
     */
    List<HouseAccountResponse> getAllHouseAccounts();
    
    /**
     * Update an existing house account.
     * 
     * @param id The account ID
     * @param request The house account request
     * @return The updated house account response
     */
    HouseAccountResponse updateHouseAccount(String id, HouseAccountRequest request);
    
    /**
     * Delete a house account.
     * 
     * @param id The account ID
     */
    void deleteHouseAccount(String id);
    
    /**
     * Update the standing status of a house account.
     * 
     * @param id The account ID
     * @param standing The new standing status
     * @return The updated house account response
     */
    HouseAccountResponse updateAccountStanding(String id, AccountStanding standing);
    
    /**
     * Find house accounts by standing status.
     * 
     * @param standing The standing status
     * @return A list of house account responses
     */
    List<HouseAccountResponse> findByAccountStanding(AccountStanding standing);
    
    /**
     * Find house accounts by customer number (case insensitive, partial match).
     * 
     * @param customerNumber The customer number to search for
     * @return A list of house account responses
     */
    List<HouseAccountResponse> findByName(String customerNumber);
    
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
     * @param id The account ID
     * @param creditLimit The new credit limit
     * @return The updated house account response
     */
    HouseAccountResponse updateCreditLimit(String id, Double creditLimit);
}
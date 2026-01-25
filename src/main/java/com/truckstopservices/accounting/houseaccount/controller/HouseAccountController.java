package com.truckstopservices.accounting.houseaccount.controller;

import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
import com.truckstopservices.accounting.houseaccount.dto.HouseAccountResponse;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;
import com.truckstopservices.accounting.houseaccount.exception.HouseAccountException;
import com.truckstopservices.accounting.houseaccount.service.HouseAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for house account operations.
 */
@RestController
@RequestMapping("accounting/house-accounts")
public class HouseAccountController {
    
    private final HouseAccountService houseAccountService;
    
    public HouseAccountController(HouseAccountService houseAccountService) {
        this.houseAccountService = houseAccountService;
    }
    
    /**
     * Create a new house account.
     * 
     * @param request The house account request
     * @return The created house account response
     */
    @PostMapping
    public ResponseEntity<HouseAccountResponse> createHouseAccount(@RequestBody HouseAccountRequest request) {
        HouseAccountResponse response = houseAccountService.createHouseAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get a house account by ID.
     * 
     * @param houseAccountId The house account ID
     * @return The house account response
     */
    @GetMapping("/{houseAccountId}")
    public ResponseEntity<HouseAccountResponse> getHouseAccount(@PathVariable String houseAccountId) {
        HouseAccountResponse response = houseAccountService.getHouseAccount(houseAccountId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all house accounts.
     * 
     * @return A list of all house account responses
     */
    @GetMapping
    public ResponseEntity<List<HouseAccountResponse>> getAllHouseAccounts() {
        List<HouseAccountResponse> responses = houseAccountService.getAllHouseAccounts();
        return ResponseEntity.ok(responses);
    }

    /**
     * Delete a house account.
     * 
     * @param houseAccountId The house account ID
     * @return A response with no content
     */
    @DeleteMapping("/{houseAccountId}")
    public ResponseEntity<Void> deleteHouseAccount(@PathVariable String houseAccountId) {
        houseAccountService.deleteHouseAccount(houseAccountId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Update the standing status of a house account.
     * 
     * @param customerNumber The customer number
     * @param body Map containing the standing status
     * @return The updated house account response
     */
    @PatchMapping("/{customerNumber}/standing")
    public ResponseEntity<HouseAccountResponse> updateAccountStanding(
            @PathVariable String customerNumber,
            @RequestBody Map<String, String> body) {
        AccountStanding standing = AccountStanding.valueOf(body.get("standing").toUpperCase());
        HouseAccountResponse response = houseAccountService.updateAccountStanding(customerNumber, standing);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update the credit limit of a house account.
     * 
     * @param customerNumber The customer number
     * @param body Map containing the credit limit
     * @return The updated house account response
     */
    @PatchMapping("/{customerNumber}/credit-limit")
    public ResponseEntity<HouseAccountResponse> updateCreditLimit(
            @PathVariable String customerNumber,
            @RequestBody Map<String, Double> body) {
        Double creditLimit = body.get("creditLimit");
        if (creditLimit == null) {
            return ResponseEntity.badRequest().build();
        }
        
        HouseAccountResponse response = houseAccountService.updateCreditLimit(customerNumber, creditLimit);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Find house accounts by standing status.
     * 
     * @param standing The standing status
     * @return A list of house account responses
     */
    @GetMapping("/standing/{standing}")
    public ResponseEntity<List<HouseAccountResponse>> findByAccountStanding(@PathVariable String standing) {
        AccountStanding accountStanding = AccountStanding.valueOf(standing.toUpperCase());
        List<HouseAccountResponse> responses = houseAccountService.findByAccountStanding(accountStanding);
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Find house accounts by name.
     * 
     * @param customerNumber The name to search for
     * @return A list of house account responses
     */
    @GetMapping("/search/{customerNumber}")
    public ResponseEntity<List<HouseAccountResponse>> findByCustomerName(@PathVariable String customerNumber) {
        List<HouseAccountResponse> responses = houseAccountService.findByName(customerNumber);
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Find house accounts by phone number.
     * 
     * @param phoneNumber The phone number
     * @return A list of house account responses
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<HouseAccountResponse>> findByPhoneNumber(@PathVariable String phoneNumber) {
        List<HouseAccountResponse> responses = houseAccountService.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(responses);
    }
}
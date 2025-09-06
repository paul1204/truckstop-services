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
    
    @Autowired
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
        try {
            HouseAccountResponse response = houseAccountService.createHouseAccount(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (HouseAccountException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Get a house account by ID.
     * 
     * @param houseAccountId The house account ID
     * @return The house account response
     */
    @GetMapping("/{houseAccountId}")
    public ResponseEntity<HouseAccountResponse> getHouseAccount(@PathVariable String houseAccountId) {
        try {
            HouseAccountResponse response = houseAccountService.getHouseAccount(houseAccountId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (HouseAccountException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Get all house accounts.
     * 
     * @return A list of all house account responses
     */
    @GetMapping
    public ResponseEntity<List<HouseAccountResponse>> getAllHouseAccounts() {
        List<HouseAccountResponse> responses = houseAccountService.getAllHouseAccounts();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * Delete a house account.
     * 
     * @param houseAccountId The house account ID
     * @return A response with no content
     */
    @DeleteMapping("/{houseAccountId}")
    public ResponseEntity<Void> deleteHouseAccount(@PathVariable String houseAccountId) {
        try {
            houseAccountService.deleteHouseAccount(houseAccountId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (HouseAccountException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        try {
            AccountStanding standing = AccountStanding.valueOf(body.get("standing").toUpperCase());
            HouseAccountResponse response = houseAccountService.updateAccountStanding(customerNumber, standing);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (HouseAccountException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        try {
            Double creditLimit = body.get("creditLimit");
            if (creditLimit == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            HouseAccountResponse response = houseAccountService.updateCreditLimit(customerNumber, creditLimit);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (HouseAccountException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Find house accounts by standing status.
     * 
     * @param standing The standing status
     * @return A list of house account responses
     */
    @GetMapping("/standing/{standing}")
    public ResponseEntity<List<HouseAccountResponse>> findByAccountStanding(@PathVariable String standing) {
        try {
            AccountStanding accountStanding = AccountStanding.valueOf(standing.toUpperCase());
            List<HouseAccountResponse> responses = houseAccountService.findByAccountStanding(accountStanding);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
        return new ResponseEntity<>(responses, HttpStatus.OK);
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
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
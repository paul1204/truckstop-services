package com.truckstopservices.accounting.houseaccount.service;

import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
import com.truckstopservices.accounting.houseaccount.dto.HouseAccountResponse;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;
import com.truckstopservices.accounting.houseaccount.exception.HouseAccountException;
import com.truckstopservices.accounting.houseaccount.repository.HouseAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the HouseAccountService interface.
 */
@Service
public class HouseAccountServiceImpl implements HouseAccountService {
    
    private final HouseAccountRepository houseAccountRepository;
    
    @Autowired
    public HouseAccountServiceImpl(HouseAccountRepository houseAccountRepository) {
        this.houseAccountRepository = houseAccountRepository;
    }
    
    @Override
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
    
    @Override
    @Transactional(readOnly = true)
    public HouseAccountResponse getHouseAccount(String id) {
        HouseAccount houseAccount = findAccountById(id);
        return HouseAccountResponse.fromEntity(houseAccount);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountResponse> getAllHouseAccounts() {
        return houseAccountRepository.findAll().stream()
            .map(HouseAccountResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    @Override
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
    
    @Override
    @Transactional
    public void deleteHouseAccount(String id) {
        if (!houseAccountRepository.existsById(id)) {
            throw HouseAccountException.accountNotFound(id);
        }
        
        houseAccountRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public HouseAccountResponse updateAccountStanding(String id, AccountStanding standing) {
        HouseAccount houseAccount = findAccountById(id);
        
        // Update standing and handle reset of good standing duration if needed
        houseAccount.setAccountStanding(standing);
        
        // Save and return
        HouseAccount updatedAccount = houseAccountRepository.save(houseAccount);
        return HouseAccountResponse.fromEntity(updatedAccount);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountResponse> findByAccountStanding(AccountStanding standing) {
        return houseAccountRepository.findByAccountStanding(standing).stream()
            .map(HouseAccountResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountResponse> findByName(String name) {
        return houseAccountRepository.findByCompanyName(name).stream()
            .map(HouseAccountResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HouseAccountResponse> findByPhoneNumber(String phoneNumber) {
        return houseAccountRepository.findByPhoneNumber(phoneNumber).stream()
            .map(HouseAccountResponse::fromEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public HouseAccountResponse updateCreditLimit(String id, Double creditLimit) {
        HouseAccount houseAccount = findAccountById(id);
        
        houseAccount.setCreditLimit(creditLimit);
        houseAccount.setUpdatedAt(LocalDateTime.now());
        
        // Save and return
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
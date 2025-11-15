package com.truckstopservices.accounting.houseaccount.repository;

import com.truckstopservices.accounting.houseaccount.entity.HouseAccount;
import com.truckstopservices.accounting.houseaccount.entity.HouseAccount.AccountStanding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for house account operations.
 */
@Repository
public interface HouseAccountRepository extends JpaRepository<HouseAccount, String> {
    
    /**
     * Find all accounts with a specific standing status.
     * 
     * @param accountStanding The account standing status to filter by
     * @return A list of house accounts with the specified standing
     */
    List<HouseAccount> findByAccountStanding(AccountStanding accountStanding);
    
    /**
     * Find accounts by company name containing the search term (case insensitive).
     * 
     * @param name The company name search term
     * @return A list of house accounts with company names containing the search term
     */
    List<HouseAccount> findByCompanyName(String name);
    
    /**
     * Find accounts by phone number.
     * 
     * @param phoneNumber The phone number to search for
     * @return A list of house accounts with the specified phone number
     */
    List<HouseAccount> findByPhoneNumber(String phoneNumber);
    
    /**
     * Find accounts with a credit limit greater than or equal to the specified amount.
     * 
     * @param creditLimit The minimum credit limit to filter by
     * @return A list of house accounts with credit limits greater than or equal to the specified amount
     */
    List<HouseAccount> findByCreditLimitGreaterThanEqual(Double creditLimit);
    
    /**
     * Find accounts with a credit limit less than or equal to the specified amount.
     * 
     * @param creditLimit The maximum credit limit to filter by
     * @return A list of house accounts with credit limits less than or equal to the specified amount
     */
    List<HouseAccount> findByCreditLimitLessThanEqual(Double creditLimit);

    HouseAccount findByHouseAccountId(String houseAccountId);
}
package com.truckstopservices.accounting.houseaccount.exception;

/**
 * Custom exception for house account operations.
 */
public class HouseAccountException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public HouseAccountException(String message) {
        super(message);
    }
    
    public HouseAccountException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates an exception for when an account is not found.
     * 
     * @param customerNumber The customer number that was not found
     * @return A new HouseAccountException
     */
    public static HouseAccountException accountNotFound(String customerNumber) {
        return new HouseAccountException("House account not found with customer number: " + customerNumber);
    }
    
    /**
     * Creates an exception for when an account already exists.
     * 
     * @param customerNumber The customer number that already exists
     * @return A new HouseAccountException
     */
    public static HouseAccountException accountAlreadyExists(String customerNumber) {
        return new HouseAccountException("House account already exists with customer number: " + customerNumber);
    }
    
    /**
     * Creates an exception for when a credit limit is exceeded.
     * 
     * @param customerNumber The customer number
     * @param currentLimit The current credit limit
     * @return A new HouseAccountException
     */
    public static HouseAccountException creditLimitExceeded(String customerNumber, Double currentLimit) {
        return new HouseAccountException("Credit limit exceeded for customer number: " + customerNumber + 
                                        ". Current limit: $" + currentLimit);
    }
    
    /**
     * Creates an exception for when an account is not in good standing.
     * 
     * @param customerNumber The customer number
     * @param standing The current standing status
     * @return A new HouseAccountException
     */
    public static HouseAccountException accountNotInGoodStanding(String customerNumber, String standing) {
        return new HouseAccountException("Account is not in good standing for customer number: " + customerNumber + 
                                        ". Current standing: " + standing);
    }
}
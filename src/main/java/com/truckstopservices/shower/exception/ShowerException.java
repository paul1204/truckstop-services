package com.truckstopservices.shower.exception;

public class ShowerException extends RuntimeException {
    
    public ShowerException(String message) {
        super(message);
    }
    
    public ShowerException(String message, Throwable cause) {
        super(message, cause);
    }
}
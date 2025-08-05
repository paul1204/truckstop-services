package com.truckstopservices.accounting.houseaccount.config;

import com.truckstopservices.accounting.houseaccount.dto.HouseAccountRequest;
import com.truckstopservices.accounting.houseaccount.service.HouseAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to initialize house accounts.
 * This is a temporary solution until data is migrated to SQL scripts.
 */
@Configuration
public class HouseAccountConfig {
    
    private final HouseAccountService houseAccountService;
    
    @Autowired
    public HouseAccountConfig(HouseAccountService houseAccountService) {
        this.houseAccountService = houseAccountService;
    }
    
    @Bean
    public CommandLineRunner initializeHouseAccounts() {
        return args -> {
            // Create sample house accounts
            createHouseAccount("ACME001", "ACME Trucking", "555-123-4567", "123 Main St, Truckville, TX 75001", 1000.00);
            createHouseAccount("FAST003", "Fast Freight Inc", "555-234-5678", "456 Highway Dr, Speedway, CA 90210", 1500.00);
            createHouseAccount("HAUL003", "Heavy Haulers LLC", "555-345-6789", "789 Industrial Blvd, Loadtown, NY 10001", 2000.00);
            createHouseAccount("ROAD004", "Roadrunner Transport", "555-456-7890", "321 Express Way, Quickville, FL 33101", 1200.00);
            createHouseAccount("SHIP005", "Shipment Solutions", "555-567-8901", "654 Logistics Ave, Cargotown, IL 60601", 800.00);
            
            System.out.println("House accounts initialized successfully!");
        };
    }
    
    /**
     * Helper method to create a house account.
     */
    private void createHouseAccount(String customerNumber, String name, String phoneNumber, String address, Double creditLimit) {
        try {
            HouseAccountRequest request = new HouseAccountRequest();
            request.setCustomerNumber(customerNumber);
            request.setName(name);
            request.setPhoneNumber(phoneNumber);
            request.setAddress(address);
            request.setCreditLimit(creditLimit);
            
            houseAccountService.createHouseAccount(request);
            System.out.println("Created house account: " + name);
        } catch (Exception e) {
            System.err.println("Failed to create house account " + name + ": " + e.getMessage());
        }
    }
}
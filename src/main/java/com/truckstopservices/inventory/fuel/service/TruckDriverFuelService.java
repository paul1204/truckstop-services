package com.truckstopservices.inventory.fuel.service;

import com.truckstopservices.accounting.accountsPayable.service.implementation.AccountsPayableImplementation;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.accounting.pos.dto.Receipt;
import com.truckstopservices.accounting.pos.enums.SalesType;
import com.truckstopservices.accounting.pos.service.POSService;
import com.truckstopservices.inventory.fuel.dto.FuelChartDataResponse;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleRequest;
import com.truckstopservices.inventory.fuel.dto.FuelSaleResponse;
import com.truckstopservices.inventory.fuel.entity.*;
import com.truckstopservices.inventory.fuel.repository.*;
import com.truckstopservices.inventory.fuel.exception.FuelSaleException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TruckDriverFuelService {

    private final DieselRepository dieselRepository;
    private final FuelDeliveryRepository fuelDeliveryRepository;
    private final AccountsPayableImplementation accountsPayableImplementation;
    private final POSService posService;

    @Autowired
    public TruckDriverFuelService(
            DieselRepository dieselRepository,
            FuelDeliveryRepository fuelDeliveryRepository,
            AccountsPayableImplementation accountsPayableImplementation,
            POSService posService) {
        this.dieselRepository = dieselRepository;
        this.fuelDeliveryRepository = fuelDeliveryRepository;
        this.accountsPayableImplementation = accountsPayableImplementation;
        this.posService = posService;
    }

    public List<FuelInventoryResponse> getAllDieselInventory() {
        List<FuelInventoryResponse> fuelInventoryResponses = new ArrayList<>();
        // Calculate total diesel gallons
        double totalDieselGallons = 0;
        List<Diesel> dieselList = dieselRepository.findAll();
        for (Diesel diesel : dieselList) {
            if (diesel.getAvailableGallons() > 0) {
                totalDieselGallons += diesel.getAvailableGallons();
            }
        }
        fuelInventoryResponses.add(new FuelInventoryResponse("Diesel", totalDieselGallons));
        return fuelInventoryResponses;
    }

    @Transactional
    public FuelSaleResponse updateDieselInventoryFIFOSales(double gallonsSold) throws FuelSaleException {
        if (gallonsSold <= 0) {
            throw new FuelSaleException("Diesel Fuel Tank IS EMPTY!");
        }
        double remainingGallonsToSell = gallonsSold;
        double totalSaleAmount = 0;
        // Get the oldest diesel inventory with available gallons (FIFO)
        Optional<Diesel> oldestDieselOptional = dieselRepository.findFIFOAvailableGallons();
        if (oldestDieselOptional.isEmpty()) {
            throw new FuelSaleException("No diesel inventory available");
        }
        Diesel oldestDiesel = oldestDieselOptional.get();
        // If the oldest diesel has enough gallons
        if (oldestDiesel.getAvailableGallons() >= remainingGallonsToSell) {
            oldestDiesel.updateGallonsReduceInventorySales(remainingGallonsToSell);
            totalSaleAmount = oldestDiesel.calculateTotalPrice(remainingGallonsToSell);
            dieselRepository.save(oldestDiesel);
            remainingGallonsToSell = 0;
        } else {
            // If the oldest diesel doesn't have enough gallons, use what's available
            double availableGallons = oldestDiesel.getAvailableGallons();
            totalSaleAmount = oldestDiesel.calculateTotalPrice(availableGallons);
            remainingGallonsToSell -= availableGallons;
            // Mark this diesel as inactive (all gallons used)
            oldestDiesel.updateGallonsReduceInventorySales(availableGallons);
            oldestDiesel.setFlagInactive();
            dieselRepository.save(oldestDiesel);
            // If we still need more gallons, get the next diesel in line
            if (remainingGallonsToSell > 0) {
                Optional<Diesel> nextDieselOptional = dieselRepository.findFIFOAvailableGallons();
                if (nextDieselOptional.isEmpty()) {
                    throw new FuelSaleException("Not enough diesel inventory available");
                }
                Diesel nextDiesel = nextDieselOptional.get();
                if (nextDiesel.getAvailableGallons() >= remainingGallonsToSell) {
                    nextDiesel.updateGallonsReduceInventorySales(remainingGallonsToSell);
                    totalSaleAmount += nextDiesel.calculateTotalPrice(remainingGallonsToSell);
                    dieselRepository.save(nextDiesel);
                    remainingGallonsToSell = 0;
                } else {
                    throw new FuelSaleException("Not enough diesel inventory available");
                }
            }
        }
        Receipt receipt = posService.createPOSRecord(totalSaleAmount, SalesType.FUEL);
        return new FuelSaleResponse(
                0, // cetane numbers
                gallonsSold,
                totalSaleAmount,
                "Thank you for choosing our truck stop for your diesel needs!",
                receipt
        );
    }
    public List<FuelChartDataResponse> getDieselInventoryChartData() {
        List<FuelInventoryResponse> fuelInventory = getAllFuelInventory();
        List<FuelChartDataResponse> chartData = new ArrayList<>();

        for (int i = 0; i < fuelInventory.size(); i++) {
            FuelInventoryResponse fuel = fuelInventory.get(i);
            chartData.add(new FuelChartDataResponse(
                    i,                      // id
                    "Total Gallons",        // series
                    fuel.fuelName(),        // group
                    fuel.totalGallons()     // value
            ));
        }
        return chartData;
    }
        public List<FuelInventoryResponse> getAllFuelInventory() {
            List<FuelInventoryResponse> fuelInventoryList = new ArrayList<>();
            fuelInventoryList.addAll(
                    dieselRepository.findAll().stream()
                            .map(diesel -> new FuelInventoryResponse("Diesel", diesel.getAvailableGallons()))
                            .toList());

        return fuelInventoryList.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        FuelInventoryResponse::fuelName,
                        java.util.stream.Collectors.summingDouble(FuelInventoryResponse::totalGallons)))
                .entrySet().stream()
                .map(entry -> new FuelInventoryResponse(entry.getKey(), entry.getValue()))
                .toList();
    }
}
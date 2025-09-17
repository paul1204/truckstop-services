package com.truckstopservices.inventory.fuel.service;

import com.truckstopservices.accounting.invoice.service.implementation.InvoiceServiceImpl;
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
//import com.truckstopservices.inventory.fuel.repository.FuelRepository;
import com.truckstopservices.inventory.fuel.exception.FuelSaleException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FuelService {

    private static final Logger log = LoggerFactory.getLogger(FuelService.class);

    @Autowired
    private DieselRepository dieselRepository;

    @Autowired
    private RegularFuelRepository regularFuelRepository;

    @Autowired
    private MidGradeFuelRepository midGradeFuelRepository;

    @Autowired
    private PremimumFuelRepository premimumFuelRepository;

    @Autowired
    private FuelDeliveryRepository fuelDeliveryRepository;

    @Autowired
    private InvoiceServiceImpl invoiceService;

    @Autowired
    private POSService posService;

    public FuelService(DieselRepository dieselRepository, RegularFuelRepository regularFuelRepository,
                       MidGradeFuelRepository midGradeFuelRepository, PremimumFuelRepository premimumFuelRepository,
                       InvoiceServiceImpl invoiceService, POSService posService) {
        this.dieselRepository = dieselRepository;
        this.regularFuelRepository = regularFuelRepository;
        this.midGradeFuelRepository = midGradeFuelRepository;
        this.premimumFuelRepository = premimumFuelRepository;
        this.invoiceService = invoiceService;
        this.posService = posService;
    }

    public List<FuelInventoryResponse> getAllFuelInventory() {
        List<FuelInventoryResponse> fuelInventoryList = new ArrayList<>();
        fuelInventoryList.addAll(
                dieselRepository.findAll().stream()
                        .map(diesel -> new FuelInventoryResponse("Diesel", diesel.getAvailableGallons()))
                        .toList());
        fuelInventoryList.addAll(
                regularFuelRepository.findAll().stream()
                        .map(regularOctane -> new FuelInventoryResponse("87", regularOctane.getAvailableGallons()))
                        .toList());
        fuelInventoryList.addAll(
                midGradeFuelRepository.findAll().stream()
                        .map(midGradeOctane -> new FuelInventoryResponse("89", midGradeOctane.getAvailableGallons()))
                        .toList());
        fuelInventoryList.addAll(
                premimumFuelRepository.findAll().stream()
                        .map(premiumOctane -> new FuelInventoryResponse("93", premiumOctane.getAvailableGallons()))
                        .toList());
//        return fuelInventoryList;
        // Aggregate by fuelName and sum totalGallons
        return fuelInventoryList.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        FuelInventoryResponse::fuelName,
                        java.util.stream.Collectors.summingDouble(FuelInventoryResponse::totalGallons)))
                .entrySet().stream()
                .map(entry -> new FuelInventoryResponse(entry.getKey(), entry.getValue()))
                .toList();
    }
    


    public void updateFuelInventoryDeductAvailableGallonsFromSales(Double[] fuelSales) {
        RegularOctane regularOctane = regularFuelRepository.findByOctane(87)
                .orElseThrow(() -> new EntityNotFoundException("Regular Not Found"));
        regularOctane.updateGallonsReduceInventorySales(fuelSales[0]);
        regularFuelRepository.save(regularOctane);
        MidGradeOctane midGrade = midGradeFuelRepository.findByOctane(89)
                .orElseThrow(() -> new EntityNotFoundException("Mid Grade Not Found"));
        midGrade.updateGallonsReduceInventorySales(fuelSales[1]);
        midGradeFuelRepository.save(midGrade);
        PremiumOctane premiumOctane = premimumFuelRepository.findByOctane(91)
                .orElseThrow(() -> new EntityNotFoundException("Premium Grade Not Found"));
        premiumOctane.updateGallonsReduceInventorySales(fuelSales[2]);
        premimumFuelRepository.save(premiumOctane);
        Diesel diesel = dieselRepository.findByOctane(40)
                .orElseThrow(() -> new EntityNotFoundException("Diesel Not Found"));
        diesel.updateGallonsReduceInventorySales(fuelSales[3]);
        dieselRepository.save(diesel);
        //return new Fuel[]{regularOctane,premiumOctane,diesel};
    }

    @Transactional
    public FuelDeliveryResponse<FuelDelivery> updateFuelDeliveryRepo(FuelDelivery fuelDelivery) throws Exception {
        try {
            FuelDelivery savedDelivery = fuelDeliveryRepository.save(fuelDelivery);
            try {
                Diesel d = savedDelivery.getDieselOrder();
                RegularOctane r = savedDelivery.getRegularOctaneOrder();
                PremiumOctane p = savedDelivery.getPremiumOctaneOrder();
                String deliveryDate = savedDelivery.getDeliveryDate();

                if (d != null) {
                    log.info("fuel_delivery_saved type=Diesel deliveryId={} octane={} totalGallons={} pricePerGallon={} deliveryDate={}",
                            d.getDelivery_id(), d.getOctane(), d.getTotalGallons(), d.getPricePerGallon(), deliveryDate);
                }
                if (r != null) {
                    log.info("fuel_delivery_saved type=Regular octane={} totalGallons={} pricePerGallon={} deliveryDate={}",
                            r.getOctane(), r.getTotalGallons(), r.getPricePerGallon(), deliveryDate);
                }
                if (p != null) {
                    log.info("fuel_delivery_saved type=Premium octane={} totalGallons={} pricePerGallon={} deliveryDate={}",
                            p.getOctane(), p.getTotalGallons(), p.getPricePerGallon(), deliveryDate);
                }
            } catch (Exception e) {
                log.warn("fuel_delivery_saved logging_error message={}", e.getMessage());
            }
            //Need to update additional fields past initial save to repo
            updateFuelInventoryFromDelivery(savedDelivery);

            // Calculate total amount for the invoice
            double totalAmount = calculateTotalAmount(fuelDelivery);

            // Create and save the invoice
            Invoice vendorInvoice = invoiceService.createInvoice(
                fuelDelivery.getCompanyName(),
                fuelDelivery.getDeliveryDate(),
                totalAmount
            );

            return new FuelDeliveryResponse<>(true, "Fuel Successfully Delivered!", new FuelDelivery[0], vendorInvoice);
        } catch (DataAccessException e) {
            throw new DataAccessResourceFailureException("Failed to update fuel delivery: " + e.getMessage(), e);
        }
    }

    /**
     * Calculate the total amount for the invoice based on the fuel delivery
     * @param fuelDelivery The fuel delivery
     * @return The total amount
     */
    private double calculateTotalAmount(FuelDelivery fuelDelivery) {
        double dieselAmount = 0;
        double regularAmount = 0;
        double premiumAmount = 0;

        if (fuelDelivery.getDieselOrder() != null) {
            dieselAmount = fuelDelivery.getDieselOrder().getPricePerGallon() * fuelDelivery.getDieselOrder().getTotalGallons();
        }

        if (fuelDelivery.getRegularOctaneOrder() != null) {
            regularAmount = fuelDelivery.getRegularOctaneOrder().getPricePerGallon() * fuelDelivery.getRegularOctaneOrder().getTotalGallons();
        }

        if (fuelDelivery.getPremiumOctaneOrder() != null) {
            premiumAmount = fuelDelivery.getPremiumOctaneOrder().getPricePerGallon() * fuelDelivery.getPremiumOctaneOrder().getTotalGallons();
        }

        return dieselAmount + regularAmount + premiumAmount;
    }

    private void updateFuelInventoryFromDelivery(FuelDelivery fuelDelivery) throws Exception {
        //updateDieselFuelDelivery(fuelDelivery.getDieselOrder().getDelivery_id(),fuelDelivery.getDieselOrder().getTotalGallons());
        String deliveryDate = fuelDelivery.getDeliveryDate();
        updateDieselFuelDelivery(fuelDelivery.getDieselOrder(), deliveryDate);
        updateRegularOctaneFuelDelivery(fuelDelivery.getRegularOctaneOrder(), deliveryDate);
        updatePremiumOctaneFuelDelivery(fuelDelivery.getPremiumOctaneOrder(), deliveryDate);
    }

    private Diesel updateDieselFuelDelivery(Diesel newDieselOrder, String deliveryDate) throws Exception {
        return dieselRepository.findById(newDieselOrder.getDelivery_id())
                .map(diesel -> {
                    diesel.updateGallonsAddInventory(newDieselOrder.getTotalGallons());
                    diesel.setDeliveryDate(deliveryDate);
                    // diesel.setNextDelivery_id(newDieselOrder.getDelivery_id() + 1 );
                    return dieselRepository.save(diesel);
                })
                //Throw Better Error!!
                .orElseThrow(() -> new Exception("Error, could not accept Fuel Delivery "));
    }

    private RegularOctane updateRegularOctaneFuelDelivery(RegularOctane newRegularOrder, String deliveryDate) throws Exception {
        return regularFuelRepository.findById(newRegularOrder.getDelivery_id())
                .map(regularOctane -> {
                    regularOctane.updateGallonsAddInventory(newRegularOrder.getTotalGallons());
                    regularOctane.setDeliveryDate(deliveryDate);
                    return regularFuelRepository.save(regularOctane);
                })
                //Throw Better Error!!
                .orElseThrow(() -> new Exception("Error, could not accept Fuel Delivery "));
    }

    private PremiumOctane updatePremiumOctaneFuelDelivery(PremiumOctane newPremiumOrder, String deliveryDate) throws Exception {
        return premimumFuelRepository.findById(newPremiumOrder.getDelivery_id())
                .map(premiumOctane -> {
                    premiumOctane.updateGallonsAddInventory(newPremiumOrder.getTotalGallons());
                    premiumOctane.setDeliveryDate(deliveryDate);
                    return premimumFuelRepository.save(premiumOctane);
                })
                //Throw Better Error!!
                .orElseThrow(() -> new Exception("Error, could not accept Fuel Delivery "));
    }

    @Transactional
    public FuelSaleResponse updateDieselInventoryFIFOSales(double gallonsSold) throws FuelSaleException {
        log.info("fuel_request type=Diesel gallons={}", gallonsSold);
        Optional<Diesel> dieselFirstRecord = dieselRepository.findFIFOAvailableGallons();
        if (!dieselFirstRecord.isPresent()) {
            throw new FuelSaleException("No available fuel to consume. there are " + gallonsSold + " gallons unaccounted for");
        }
        Diesel fifoDiesel = dieselFirstRecord.get();
        double originalFifoGallonState;
        if (fifoDiesel.getAvailableGallons() > gallonsSold) {
            fifoDiesel.updateGallonsReduceInventorySales(gallonsSold);
            dieselRepository.save(fifoDiesel);

            double totalPrice = gallonsSold * 1.99;
            FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(fifoDiesel.getOctane(), gallonsSold, totalPrice, "Diesel Fuel Updated");


            Receipt receipt = posService.createPOSRecord(totalPrice, SalesType.FUEL);

            return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
        }
        if (fifoDiesel.getAvailableGallons() <= gallonsSold) {
            Optional<Diesel> dieselSecondRecord = dieselRepository.findNextFifoNextAvailableGallons();
            double negativeCarryOver = fifoDiesel.getAvailableGallons() - gallonsSold;
            //In event of Rollback, we can return the first available gallons to report to users that the inventory was not consumed.
            originalFifoGallonState = fifoDiesel.getAvailableGallons();
            fifoDiesel.setAvailableGallons(fifoDiesel.getAvailableGallons() - fifoDiesel.getAvailableGallons());
            if (dieselSecondRecord.isPresent()) {
                Diesel newFifoDieselBatch = dieselSecondRecord.get();
                double updatedNextBatchOfGallons = newFifoDieselBatch.getAvailableGallons() + negativeCarryOver;
                newFifoDieselBatch.setAvailableGallons(updatedNextBatchOfGallons);
                dieselRepository.save(newFifoDieselBatch);
                log.info("Current Batch Ending.. Switch to next available Batch type=Diesel gallons={}", updatedNextBatchOfGallons);

                //Foreign Key Constraint when moving to historical table
                //Issue #4
                //HistoricalFuel historicalFuel = new HistoricalFuel();
                //BeanUtils.copyProperties(fifoDiesel, historicalFuel);
                //historicalFuelRepository.save(historicalFuel);
                //dieselRepository.delete(fifoDiesel);

                fifoDiesel.setFlagInactive();

                double totalPrice = gallonsSold * 1.99;
                FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(
                    fifoDiesel.getOctane(), 
                    gallonsSold, 
                    totalPrice, 
                    "Diesel Fuel Updated. New Batch of Fuel being used. Delivery ID: " + newFifoDieselBatch.getDelivery_id().toString()
                );

                // Create POS record and get receipt
                Receipt receipt = posService.createPOSRecord(totalPrice, SalesType.FUEL);

                // Return response with receipt
                return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
            }
            if (!dieselSecondRecord.isPresent() && fifoDiesel.getAvailableGallons() == 0) {
                throw new FuelSaleException("Next batch of fuel is unavailable. " + (negativeCarryOver * -1) + " gallons are not accounted for." +
                        "Due to Transaction Failure, a rollback occurred. " + originalFifoGallonState + " needs to be also accounted for.");
            }
        }

        FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(0, 0, 0, "No fuel was sold, inventory unchanged.");
        Receipt receipt = posService.createPOSRecord(0, SalesType.FUEL);
        return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
    }

    @Transactional
    public FuelSaleResponse updateRegularOctaneInventoryFIFOSales(double gallonsSold) {
        log.info("fuel_request type=Regular gallons={}", gallonsSold);
        Optional<RegularOctane> regularOctaneFirstRecord = regularFuelRepository.findFIFOAvailableGallons();
        if (!regularOctaneFirstRecord.isPresent()) {
            throw new FuelSaleException("No available fuel to consume. there are " + gallonsSold + " gallons unaccounted for");
        }
        RegularOctane fifoRegularOctane = regularOctaneFirstRecord.get();
        double originalFifoGallonState;
        if (fifoRegularOctane.getAvailableGallons() > gallonsSold) {
            fifoRegularOctane.updateGallonsReduceInventorySales(gallonsSold);
            regularFuelRepository.save(fifoRegularOctane);

            double totalPrice = gallonsSold * 1.99;
            FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(fifoRegularOctane.getOctane(), gallonsSold, totalPrice, "Regular Fuel Updated");

            // Create POS record and get receipt
            Receipt receipt = posService.createPOSRecord(totalPrice, SalesType.FUEL);

            // Return response with receipt
            return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
        }
        if (fifoRegularOctane.getAvailableGallons() <= gallonsSold) {
            double negativeCarryOver = fifoRegularOctane.getAvailableGallons() - gallonsSold;
            //In event of Rollback, we can return the first available gallons to report to users that the inventory was not consumed.
            originalFifoGallonState = fifoRegularOctane.getAvailableGallons();
            fifoRegularOctane.setAvailableGallons(fifoRegularOctane.getAvailableGallons() - fifoRegularOctane.getAvailableGallons());
            Optional<RegularOctane> regularOctaneNextAvailableBatch = regularFuelRepository.findNextFifoNextAvailableGallons();
            if (regularOctaneNextAvailableBatch.isPresent()) {
                RegularOctane newFifoRegularBatch = regularOctaneNextAvailableBatch.get();
                double updatedNextBatchOfGallons = newFifoRegularBatch.getAvailableGallons() + negativeCarryOver;
                newFifoRegularBatch.setAvailableGallons(updatedNextBatchOfGallons);
                regularFuelRepository.save(newFifoRegularBatch);
                log.info("Current Batch Ending.. Switch to next available Batch type=Regular gallons={}", updatedNextBatchOfGallons);
                fifoRegularOctane.setFlagInactive();

                double totalPrice = gallonsSold * 1.99;
                FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(
                    fifoRegularOctane.getOctane(), 
                    gallonsSold, 
                    totalPrice, 
                    "Regular Fuel Updated. New Batch of Fuel being used. Delivery ID: " + newFifoRegularBatch.getDelivery_id().toString()
                );

                // Create POS record and get receipt
                Receipt receipt = posService.createPOSRecord(totalPrice, SalesType.FUEL);

                // Return response with receipt
                return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
            }
            if (!regularOctaneNextAvailableBatch.isPresent() && fifoRegularOctane.getAvailableGallons() == 0) {
                throw new FuelSaleException("Next batch of fuel is unavailable. " + (negativeCarryOver * -1) + " gallons are not accounted for." +
                        "Due to Transaction Failure, a rollback occurred. " + originalFifoGallonState + " needs to be also accounted for.");
            }
        }

        FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(0, 0, 0, "No fuel was sold, inventory unchanged.");
        // Create a dummy receipt for the error case
        Receipt receipt = posService.createPOSRecord(0, SalesType.FUEL);
        return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
    }

    @Transactional
    public FuelSaleResponse updatePremiumOctaneInventoryFIFOSales(double gallonsSold) {
        log.info("fuel_request type=Premium gallons={}", gallonsSold);
        Optional<PremiumOctane> premiumOctaneFirstRecord = premimumFuelRepository.findFIFOAvailableGallons();
        if (!premiumOctaneFirstRecord.isPresent()) {
            throw new FuelSaleException("No available fuel to consume. there are " + gallonsSold + " gallons unaccounted for");
        }
        PremiumOctane fifoPremiumOctane = premiumOctaneFirstRecord.get();
        double originalFifoGallonState;
        if (fifoPremiumOctane.getAvailableGallons() > gallonsSold) {
            fifoPremiumOctane.updateGallonsReduceInventorySales(gallonsSold);
            premimumFuelRepository.save(fifoPremiumOctane);

            double totalPrice = gallonsSold * 1.99;
            FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(fifoPremiumOctane.getOctane(), gallonsSold, totalPrice, "Premium Fuel Updated");

            // Create POS record and get receipt
            Receipt receipt = posService.createPOSRecord(totalPrice, SalesType.FUEL);

            // Return response with receipt
            return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
        }
        if (fifoPremiumOctane.getAvailableGallons() <= gallonsSold) {
            double negativeCarryOver = fifoPremiumOctane.getAvailableGallons() - gallonsSold;
            //In event of Rollback, we can return the first available gallons to report to users that the inventory was not consumed.
            originalFifoGallonState = fifoPremiumOctane.getAvailableGallons();
            fifoPremiumOctane.setAvailableGallons(fifoPremiumOctane.getAvailableGallons() - fifoPremiumOctane.getAvailableGallons());
            Optional<PremiumOctane> premiumOctaneNextAvailableBatch = premimumFuelRepository.findNextFifoNextAvailableGallons();
            if (premiumOctaneNextAvailableBatch.isPresent()) {
                PremiumOctane newFifoPremiumBatch = premiumOctaneNextAvailableBatch.get();
                double updatedNextBatchOfGallons = newFifoPremiumBatch.getAvailableGallons() + negativeCarryOver;
                newFifoPremiumBatch.setAvailableGallons(updatedNextBatchOfGallons);
                premimumFuelRepository.save(newFifoPremiumBatch);
                log.info("Current Batch Ending.. Switch to next available Batch type=Premium gallons={}", updatedNextBatchOfGallons);
                fifoPremiumOctane.setFlagInactive();

                double totalPrice = gallonsSold * 1.99;
                FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(
                    fifoPremiumOctane.getOctane(), 
                    gallonsSold, 
                    totalPrice, 
                    "Premium Fuel Updated. New Batch of Fuel being used. Delivery ID: " + newFifoPremiumBatch.getDelivery_id().toString()
                );

                // Create POS record and get receipt
                Receipt receipt = posService.createPOSRecord(totalPrice, SalesType.FUEL);

                // Return response with receipt
                return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
            }
            if (!premiumOctaneNextAvailableBatch.isPresent() && fifoPremiumOctane.getAvailableGallons() == 0) {
                throw new FuelSaleException("Next batch of fuel is unavailable. " + (negativeCarryOver * -1) + " gallons are not accounted for." +
                        "Due to Transaction Failure, a rollback occurred. " + originalFifoGallonState + " needs to be also accounted for.");
            }
        }

        FuelSaleRequest fuelSaleRequest = new FuelSaleRequest(0, 0, 0, "No fuel was sold, inventory unchanged.");
        // Create a dummy receipt for the error case
        Receipt receipt = posService.createPOSRecord(0, SalesType.FUEL);
        return FuelSaleResponse.fromFuelSaleRequestAndReceipt(fuelSaleRequest, receipt);
    }
    public List<FuelChartDataResponse> getFuelInventoryChartData() {
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
}

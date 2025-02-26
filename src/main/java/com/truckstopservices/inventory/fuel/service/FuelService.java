package com.truckstopservices.inventory.fuel.service;
import com.truckstopservices.accounting.accountsPayable.service.implementation.AccountsPayableImplementation;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.inventory.fuel.dto.FuelDeliveryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.dto.FuelSaleRequest;
import com.truckstopservices.inventory.fuel.entity.*;
import com.truckstopservices.inventory.fuel.repository.*;
//import com.truckstopservices.inventory.fuel.repository.FuelRepository;
import com.truckstopservices.processing.dto.ShiftReportDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.truckstopservices.inventory.fuel.model.Fuel;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FuelService {

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
    private AccountsPayableImplementation accountsPayableImplementation;

    @Autowired
    private HistoricalFuelRepository historicalFuelRepository;


    public FuelService(DieselRepository dieselRepository, RegularFuelRepository regularFuelRepository,
                       MidGradeFuelRepository midGradeFuelRepository, PremimumFuelRepository premimumFuelRepository,
                       AccountsPayableImplementation accountsPayableImplementation, HistoricalFuelRepository historicalFuelRepository) {
        this.dieselRepository = dieselRepository;
        this.regularFuelRepository = regularFuelRepository;
        this.midGradeFuelRepository = midGradeFuelRepository;
        this.premimumFuelRepository = premimumFuelRepository;
        this.accountsPayableImplementation = accountsPayableImplementation;
        this.historicalFuelRepository = historicalFuelRepository;
    }

    public List<FuelInventoryResponse> getAllFuelInventory(){
        List<FuelInventoryResponse> fuelInventoryList = new ArrayList<>();
        fuelInventoryList.addAll(
                dieselRepository.findAll().stream()
                .map(diesel -> new FuelInventoryResponse("Diesel",diesel.getTotalGallons()))
                .toList());
        fuelInventoryList.addAll(
                regularFuelRepository.findAll().stream()
                .map(regularOctane -> new FuelInventoryResponse("87",regularOctane.getTotalGallons()))
                .toList());
        fuelInventoryList.addAll(
                midGradeFuelRepository.findAll().stream()
                .map(midGradeOctane -> new FuelInventoryResponse("89",midGradeOctane.getTotalGallons()))
                .toList());
        fuelInventoryList.addAll(
                premimumFuelRepository.findAll().stream()
                .map(premiumOctane -> new FuelInventoryResponse("93",premiumOctane.getTotalGallons()))
                .toList());

        return fuelInventoryList;
    }

    public void updateFuelInventoryDeductAvailableGallonsFromSales(Double[] fuelSales){
        RegularOctane regularOctane = regularFuelRepository.findByOctane(87)
                .orElseThrow(()-> new EntityNotFoundException("Regular Not Found"));
        regularOctane.updateGallonsReduceInventorySales(fuelSales[0]);
        regularFuelRepository.save(regularOctane);
        MidGradeOctane midGrade = midGradeFuelRepository.findByOctane(89)
                .orElseThrow(()-> new EntityNotFoundException("Mid Grade Not Found"));
        midGrade.updateGallonsReduceInventorySales(fuelSales[1]);
        midGradeFuelRepository.save(midGrade);
        PremiumOctane premiumOctane = premimumFuelRepository.findByOctane(91)
                .orElseThrow(()-> new EntityNotFoundException("Premium Grade Not Found"));
        premiumOctane.updateGallonsReduceInventorySales(fuelSales[2]);
        premimumFuelRepository.save(premiumOctane);
        Diesel diesel = dieselRepository.findByOctane(40)
                .orElseThrow(()-> new EntityNotFoundException("Diesel Not Found"));
        diesel.updateGallonsReduceInventorySales(fuelSales[3]);
        dieselRepository.save(diesel);
        //return new Fuel[]{regularOctane,premiumOctane,diesel};
    }

    @Transactional
    public FuelDeliveryResponse<FuelDelivery> updateFuelDeliveryRepo(FuelDelivery fuelDelivery) throws Exception {
        FuelDelivery savedDelivery = fuelDeliveryRepository.save(fuelDelivery); // Initial save
        //Need to update additional fields. JPA prevented from updating these fields.
        updateFuelInventoryFromDelivery(savedDelivery);
        return new FuelDeliveryResponse<>(true, "Fuel Successfully Delivered!", savedDelivery, null);
    }

    private void updateFuelInventoryFromDelivery(FuelDelivery fuelDelivery) throws Exception {
        //updateDieselFuelDelivery(fuelDelivery.getDieselOrder().getDelivery_id(),fuelDelivery.getDieselOrder().getTotalGallons());
        String deliveryDate = fuelDelivery.getDeliveryDate();
        updateDieselFuelDelivery(fuelDelivery.getDieselOrder(), deliveryDate);
        updateRegularOctaneFuelDelivery(fuelDelivery.getRegularOctaneOrder() ,deliveryDate);
        updatePremiumOctaneFuelDelivery(fuelDelivery.getPremiumOctaneOrder(),deliveryDate);
    }

    private Diesel updateDieselFuelDelivery(Diesel newDieselOrder, String deliveryDate) throws Exception{
        return dieselRepository.findById(newDieselOrder.getDelivery_id())
                .map(diesel -> {
                    diesel.updateGallonsAddInventory(newDieselOrder.getTotalGallons());
                    diesel.setDeliveryDate(deliveryDate);
                   // diesel.setNextDelivery_id(newDieselOrder.getDelivery_id() + 1 );
                    return dieselRepository.save(diesel);
                })
                //Throw Better Error!!
                .orElseThrow(()-> new Exception("Error, could not accept Fuel Delivery "));
    }

    private RegularOctane updateRegularOctaneFuelDelivery(RegularOctane newRegularOrder, String deliveryDate) throws Exception{
        return regularFuelRepository.findById(newRegularOrder.getDelivery_id())
                .map(regularOctane -> {
                    regularOctane.updateGallonsAddInventory(newRegularOrder.getTotalGallons());
                    regularOctane.setDeliveryDate(deliveryDate);
                    return regularFuelRepository.save(regularOctane);
                })
                //Throw Better Error!!
                .orElseThrow(()-> new Exception("Error, could not accept Fuel Delivery "));
    }

    private PremiumOctane updatePremiumOctaneFuelDelivery(PremiumOctane newPremiumOrder, String deliveryDate) throws Exception {
        return premimumFuelRepository.findById(newPremiumOrder.getDelivery_id())
                .map(premiumOctane -> {
                    premiumOctane.updateGallonsAddInventory(newPremiumOrder.getTotalGallons());
                    premiumOctane.setDeliveryDate(deliveryDate);
                    return premimumFuelRepository.save(premiumOctane);
                })
                //Throw Better Error!!
                .orElseThrow(()-> new Exception("Error, could not accept Fuel Delivery "));
    }

    @Transactional
    public Diesel updateDieselInventoryFIFOSales(double gallonsSold) {
        Optional<Diesel> dieselFirstRecord = dieselRepository.findFIFOAvailableGallons();
        if(!dieselFirstRecord.isPresent()) {
            return null;
        }
        Diesel fifoDiesel = dieselFirstRecord.get();
        if (fifoDiesel.getAvailableGallons() > gallonsSold) {
            fifoDiesel.updateGallonsReduceInventorySales(gallonsSold);
            dieselRepository.save(fifoDiesel);
            return fifoDiesel;
        }
        if (fifoDiesel.getAvailableGallons() < gallonsSold) {
            Optional<Diesel> dieselSecondRecord = dieselRepository.findNextFifoNextAvailableGallons();

            double negativeCarryOver = fifoDiesel.getAvailableGallons() - gallonsSold;
            fifoDiesel.setAvailableGallons(fifoDiesel.getAvailableGallons() - fifoDiesel.getAvailableGallons());
            if (dieselSecondRecord.isPresent()) {
                Diesel newFifoDieselBatch = dieselSecondRecord.get();
                //Expire available gallons using getters rather than hardcoding 0.00
                newFifoDieselBatch.setAvailableGallons(newFifoDieselBatch.getAvailableGallons() + negativeCarryOver);
                dieselRepository.save(newFifoDieselBatch);

                //Foreign Key Constraint when moving to historical table
                //Issue #4
                //HistoricalFuel historicalFuel = new HistoricalFuel();
                //BeanUtils.copyProperties(fifoDiesel, historicalFuel);
                //historicalFuelRepository.save(historicalFuel);
                //dieselRepository.delete(fifoDiesel);

                fifoDiesel.setFlagInactive();
                return newFifoDieselBatch;
            }
        }
        //Return something more meaningful.
        return null;
    }

    @Transactional
    public RegularOctane updateRegularOctaneInventoryFIFOSales(double gallonsSold) {
        Optional<RegularOctane> regularOctaneFirstRecord = regularFuelRepository.findFIFOAvailableGallons();
        if(!regularOctaneFirstRecord.isPresent()) {
            //Return something more meaningful.
            return null;
        }
        RegularOctane fifoRegularOctane = regularOctaneFirstRecord.get();
        if (fifoRegularOctane.getAvailableGallons() > gallonsSold) {
            fifoRegularOctane.updateGallonsReduceInventorySales(gallonsSold);
            regularFuelRepository.save(fifoRegularOctane);
            return fifoRegularOctane;
        }
        if (fifoRegularOctane.getAvailableGallons() < gallonsSold) {
            Optional<RegularOctane> regularOctaneNextAvailableBatch = regularFuelRepository.findNextFifoNextAvailableGallons();
            double negativeCarryOver = fifoRegularOctane.getAvailableGallons() - gallonsSold;
            fifoRegularOctane.setAvailableGallons(fifoRegularOctane.getAvailableGallons() - fifoRegularOctane.getAvailableGallons());
            if (regularOctaneNextAvailableBatch.isPresent()) {
                RegularOctane newFifoRegularBatch = regularOctaneNextAvailableBatch.get();
                //Expire available gallons using getters rather than hardcoding 0.00
                newFifoRegularBatch.setAvailableGallons(newFifoRegularBatch.getAvailableGallons() + negativeCarryOver);
                regularFuelRepository.save(newFifoRegularBatch);
                fifoRegularOctane.setFlagInactive();
                return newFifoRegularBatch;
            }
        }
        //Return something more meaningful.
        return null;
    }

    @Transactional
    public PremiumOctane updatePremiumOctaneInventoryFIFOSales(double gallonsSold) {
        Optional<PremiumOctane> premiumOctaneFirstRecord = premimumFuelRepository.findFIFOAvailableGallons();
        if(!premiumOctaneFirstRecord.isPresent()) {
            //Return something more meaningful.
            return null;
        }
        PremiumOctane fifoPremiumOctane = premiumOctaneFirstRecord.get();
        if (fifoPremiumOctane.getAvailableGallons() > gallonsSold) {
            fifoPremiumOctane.updateGallonsReduceInventorySales(gallonsSold);
            premimumFuelRepository.save(fifoPremiumOctane);
            return fifoPremiumOctane;
        }
        if (fifoPremiumOctane.getAvailableGallons() < gallonsSold) {
            Optional<PremiumOctane> premiumOctaneNextAvailableBatch = premimumFuelRepository.findNextFifoNextAvailableGallons();
            double negativeCarryOver = fifoPremiumOctane.getAvailableGallons() - gallonsSold;
            //Expire available gallons using getters rather than hardcoding 0.00
            fifoPremiumOctane.setAvailableGallons(fifoPremiumOctane.getAvailableGallons() - fifoPremiumOctane.getAvailableGallons());
            if (premiumOctaneNextAvailableBatch.isPresent()) {
                PremiumOctane newFifoRegularBatch = premiumOctaneNextAvailableBatch.get();
                //negativeCarryOver is a negative value, adding it prevents addition.
                newFifoRegularBatch.setAvailableGallons(newFifoRegularBatch.getAvailableGallons() + negativeCarryOver);
                premimumFuelRepository.save(newFifoRegularBatch);
                fifoPremiumOctane.setFlagInactive();
                return newFifoRegularBatch;
            }
        }
        //Return something more meaningful.
        return null;
    }
}
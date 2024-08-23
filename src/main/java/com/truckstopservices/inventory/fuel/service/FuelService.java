package com.truckstopservices.inventory.fuel.service;

import com.truckstopservices.inventory.fuel.dto.FuelDeliveryDto;
import com.truckstopservices.inventory.fuel.dto.FuelInventoryResponse;
import com.truckstopservices.inventory.fuel.entity.*;
import com.truckstopservices.inventory.fuel.repository.*;
//import com.truckstopservices.inventory.fuel.repository.FuelRepository;
import com.truckstopservices.processing.dto.ShiftReportDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public FuelService(DieselRepository dieselRepository, RegularFuelRepository regularFuelRepository, MidGradeFuelRepository midGradeFuelRepository, PremimumFuelRepository premimumFuelRepository) {
        this.dieselRepository = dieselRepository;
        this.regularFuelRepository = regularFuelRepository;
        this.midGradeFuelRepository = midGradeFuelRepository;
        this.premimumFuelRepository = premimumFuelRepository;
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

    public void updateFuelInventoryDeductAvailableGallonsFromSales(ShiftReportDto shiftReportDto){
        RegularOctane regularOctane = regularFuelRepository.findByOctane(87)
                .orElseThrow(()-> new EntityNotFoundException("Regular Not Found"));
        regularOctane.updateGallonsReduceInventory(shiftReportDto.fuelSaleRegular());

        MidGradeOctane midGrade = midGradeFuelRepository.findByOctane(89)
                .orElseThrow(()-> new EntityNotFoundException("Mid Grade Not Found"));
        midGrade.updateGallonsReduceInventory(shiftReportDto.fuelSalesMidGrade());

        PremiumOctane premiumOctane = premimumFuelRepository.findByOctane(91)
                .orElseThrow(()-> new EntityNotFoundException("Premium Grade Not Found"));
        premiumOctane.updateGallonsReduceInventory(shiftReportDto.fuelSalesPremium());

        Diesel diesel = dieselRepository.findByOctane(40)
                .orElseThrow(()-> new EntityNotFoundException("Diesel Not Found"));
        diesel.updateGallonsReduceInventory(shiftReportDto.fuelSalesDiesel());

    }

    //RETURN RESPONSE!
    public void updateFuelDeliveryRepo(FuelDelivery fuelDelivery) throws Exception {
        fuelDeliveryRepository.save(fuelDelivery);
        updateFuelInventoryFromDelivery(fuelDelivery);
    }
    private void updateFuelInventoryFromDelivery(FuelDelivery fuelDelivery) throws Exception {
        updateDieselFuelDelivery(fuelDelivery.getDieselQtyOrdered());
        updateRegularOctaneFuelDelivery(fuelDelivery.getRegularOctaneQtyOrdered());
        updatePremiumOctaneFuelDelivery(fuelDelivery.getPremiumOctanePricePerGallon());
    }
    public Diesel updateDieselFuelDelivery(double purchasedGallons) throws Exception{
        return dieselRepository.findByOctane(40)
                .map(diesel -> {
                    diesel.setAvailableGallons(diesel.getAvailableGallons()+ purchasedGallons);
                    return dieselRepository.save(diesel);
                })
                //Throw Better Error!!
                .orElseThrow(()-> new Exception("Error, could not accept Fuel Delivery "));
    }

    public RegularOctane updateRegularOctaneFuelDelivery(double purchasedGallons) throws Exception{
        return regularFuelRepository.findByOctane(87)
                .map(regularOctane -> {
                    regularOctane.setAvailableGallons(regularOctane.getAvailableGallons()+ purchasedGallons);
                    return regularFuelRepository.save(regularOctane);
                })
                //Throw Better Error!!
                .orElseThrow(()-> new Exception("Error, could not accept Fuel Delivery "));
    }

    public PremiumOctane updatePremiumOctaneFuelDelivery(double purchasedGallons) throws Exception {
        return premimumFuelRepository.findByOctane(91)
                .map(premiumOctane -> {
                    premiumOctane.setAvailableGallons(premiumOctane.getAvailableGallons()+ purchasedGallons);
                    return premimumFuelRepository.save(premiumOctane);
                })
                //Throw Better Error!!
                .orElseThrow(()-> new Exception("Error, could not accept Fuel Delivery "));
    }
}
